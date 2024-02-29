package com.paran.sample.service.auth;

import com.paran.sample.dto.auth.LoginReq;
import com.paran.sample.dto.auth.RegisterReq;
import com.paran.sample.dto.auth.LoginRes;
import com.paran.sample.dto.auth.RegisterRes;

public interface AuthService {
    RegisterRes register(RegisterReq req);
    LoginRes login(LoginReq req);
}
