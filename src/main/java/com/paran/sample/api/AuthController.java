package com.paran.sample.api;

import com.paran.sample.dto.auth.LoginReq;
import com.paran.sample.dto.auth.RegisterReq;
import com.paran.sample.dto.auth.LoginRes;
import com.paran.sample.dto.auth.RegisterRes;
import com.paran.sample.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterRes> register(
            @RequestBody RegisterReq req
    ) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRes> login(
            @RequestBody LoginReq req
    ) {
        return ResponseEntity.ok(authService.login(req));
    }
}
