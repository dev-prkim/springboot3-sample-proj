package com.paran.sample.domain.auth.api;

import com.paran.sample.domain.auth.dto.LoginReq;
import com.paran.sample.domain.auth.dto.RegisterReq;
import com.paran.sample.domain.auth.service.AuthService;

import com.paran.sample.domain.common.dto.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterReq req
    ) {
        return ok(ResponseWrapper.of(authService.register(req)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginReq req
    ) {
        return ok(ResponseWrapper.of(authService.login(req)));
    }
}
