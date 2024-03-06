package com.paran.sample.domain.auth.api;

import com.paran.sample.domain.auth.dto.LoginReq;
import com.paran.sample.domain.auth.dto.RegisterReq;
import com.paran.sample.domain.auth.service.AuthService;

import com.paran.sample.domain.common.dto.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterReq request
    ) {
        return ok(ResponseWrapper.of(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginReq request
    ) {
        return ok(ResponseWrapper.of(authService.login(request)));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public void logout() {
        // for swagger
    }
}
