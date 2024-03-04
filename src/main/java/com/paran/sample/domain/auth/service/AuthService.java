package com.paran.sample.domain.auth.service;

import com.paran.sample.domain.auth.dto.LoginReq;
import com.paran.sample.domain.auth.dto.RegisterReq;
import com.paran.sample.domain.auth.dto.LoginRes;
import com.paran.sample.domain.auth.dto.RegisterRes;

public interface AuthService {
    RegisterRes register(RegisterReq req);
    LoginRes login(LoginReq req);
}
