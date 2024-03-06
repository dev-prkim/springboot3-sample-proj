package com.paran.sample.domain.auth.service;

import com.paran.sample.domain.auth.dto.LoginReq;
import com.paran.sample.domain.auth.dto.RegisterReq;
import com.paran.sample.domain.auth.dto.LoginRes;
import com.paran.sample.domain.auth.dto.RegisterRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    RegisterRes register(RegisterReq request);
    LoginRes login(LoginReq request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
