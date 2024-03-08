package com.paran.sample.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paran.sample.exception.BusinessException;
import com.paran.sample.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final ObjectMapper objectMapper;

    // 모든 컨트롤러 && NotLogging 어노테이션 미설정 시 로그 수집
    @Pointcut("within(*..*Controller) && !@annotation(com.paran.sample.logging.NotLogging)")
    public void apiRestPointCut() {
    }

    @Around("apiRestPointCut()")
    public Object reqResLogging(ProceedingJoinPoint joinPoint) throws Throwable {


        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        String traceId = (String) request.getAttribute("traceId");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Map<String, String> params = getParams(request);

        String deviceType = request.getHeader("x-custom-device-type");
        String serverIp = InetAddress.getLocalHost().getHostAddress();

        ApiLoggingData apiLoggingData = new ApiLoggingData(
                traceId,
                className,
                request.getMethod(),
                request.getRequestURI(),
                methodName,
                params,
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                serverIp,
                deviceType,
                objectMapper.readTree(request.getInputStream().readAllBytes()),
                null,
                null
        );


        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            String elapsedTimeStr = "Method: " + className + "." + methodName + "() execution time: " + elapsedTime + "ms";

            ApiLoggingData logging;
            // 6.
            if (result instanceof ResponseEntity) {
                logging = apiLoggingData.withResponseBody(((ResponseEntity<?>) result).getBody())
                        .withElapsedTime(elapsedTimeStr);
            } else {
                logging = apiLoggingData.withResponseBody("{}");
            }
            log.info(objectMapper.writeValueAsString(logging));
            return result;
        } catch (Exception e) {
            log.info("{}",
                    objectMapper.writeValueAsString(
                            apiLoggingData.withResponseBody(new BusinessException(
                                    ErrorCode.NOT_DEFINED_ERR,
                                    String.format("서버에 일시적인 장애가 있습니다.(traceId:%s)", traceId)
                            ))
                    )
            );
            throw e;
        }
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> jsonObject = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String replaceParam = paramName.replace("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(paramName));
        }
        return jsonObject;
    }

}
