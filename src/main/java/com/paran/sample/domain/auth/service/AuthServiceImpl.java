package com.paran.sample.domain.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paran.sample.config.JwtService;
import com.paran.sample.domain.auth.dto.LoginReq;
import com.paran.sample.domain.auth.dto.RegisterReq;
import com.paran.sample.domain.auth.dto.LoginRes;
import com.paran.sample.domain.auth.dto.RegisterRes;
import com.paran.sample.domain.token.persistence.entity.AccessToken;
import com.paran.sample.domain.user.persistence.entity.AppUser;
import com.paran.sample.domain.token.persistence.repository.AccessTokenRepository;
import com.paran.sample.domain.user.persistence.repository.UserRepository;
import com.paran.sample.domain.token.persistence.type.TokenType;
import com.paran.sample.exception.BusinessException;
import com.paran.sample.exception.code.ErrorCode;
import com.paran.sample.exception.code.SecurityErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.springframework.util.StringUtils.containsWhitespace;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;
    private final AuthenticationManager authenticationManager;

    private static final String LOGIN_ID_PATTERN = "^[a-z0-9_]*$";

    private void validateLoginIdFormat(String input) {
        if(containsWhitespace(input)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "loginID cannot contain whitespaces.");
        }

        if(!input.matches(LOGIN_ID_PATTERN)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "loginID may only contain lowercase letters, numbers, underscores");
        }
    }

    public void validateLoginIdDuplication(String input) {
        userRepository.findByLoginId(input).ifPresent(user -> {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    String.format("The LoginID: %s already exists.", user.getLoginId()));
        });
    }

    @Transactional
    @Override
    public RegisterRes register(RegisterReq request) {
        validateLoginIdFormat(request.loginId());
        validateLoginIdDuplication(request.loginId());

        var password = passwordEncoder.encode(request.password());
        var user = request.toUserEntity(password);
        var savedUser = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveAccessToken(savedUser, jwtToken, refreshToken);

        return new RegisterRes(savedUser.getAppUserIdx(), jwtToken, refreshToken);
    }

    @Transactional
    @Override
    public LoginRes login(LoginReq request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.loginId(),
                            request.password()
                    )
            );
            var user = userRepository.findByLoginId(request.loginId())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllAccessTokens(user);
            saveAccessToken(user, jwtToken, refreshToken);
            return new LoginRes(user.getAppUserIdx(), user.getLoginId(), user.getName(), jwtToken, refreshToken);
        } catch (BadCredentialsException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        try {
            String refreshToken = authHeader.substring(7);
            String loginId = jwtService.extractUsername(refreshToken);
            var user = userRepository.findByLoginId(loginId).orElseThrow();
            var isTokenValid = accessTokenRepository.findByRefreshToken(refreshToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if(!isTokenValid
                    || !jwtService.isTokenValid(refreshToken, user)) {
                throw new Exception("unavailable refresh token");
            }

            // 새로운 토큰으로 갱신 & 리프레시 토큰 갱신 (기존토큰 및 리프레시토큰 사용 불가)
            String newToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            updateAccessToken(newToken, newRefreshToken, refreshToken);
            var authResponse = new LoginRes(user.getAppUserIdx(), user.getLoginId(), user.getName(), newToken, newRefreshToken);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        } catch (Exception e) {
            SecurityErrorCode err = SecurityErrorCode.UNAVAILABLE_REFRESH_TOKEN;
            var problemDetail = ProblemDetail.forStatusAndDetail(err.getStatus(), err.getMessage());
            problemDetail.setTitle(err.getCode());

            response.setStatus(err.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(new ObjectMapper().writeValueAsString(problemDetail));
        }

    }

    private void saveAccessToken(AppUser user, String jwtToken, String refreshToken) {
        var accessToken = AccessToken.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        accessTokenRepository.save(accessToken);
    }

    private void revokeAllAccessTokens(AppUser user) {
        var validUserTokens = accessTokenRepository.findAccessTokensByUserIdx(user.getAppUserIdx());
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        accessTokenRepository.saveAll(validUserTokens);
    }

    private void updateAccessToken(String newJwtToken, String newRefreshToken, String refreshToken) {
        accessTokenRepository.findByRefreshToken(refreshToken)
                .ifPresent(t -> {
                    t.setToken(newJwtToken);
                    t.setRefreshToken(newRefreshToken);
                });
    }

}
