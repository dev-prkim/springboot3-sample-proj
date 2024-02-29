package com.paran.sample.service.auth;

import com.paran.sample.config.JwtService;
import com.paran.sample.dto.auth.LoginReq;
import com.paran.sample.dto.auth.RegisterReq;
import com.paran.sample.dto.auth.LoginRes;
import com.paran.sample.dto.auth.RegisterRes;
import com.paran.sample.persistence.entity.token.AccessToken;
import com.paran.sample.persistence.entity.user.User;
import com.paran.sample.persistence.repository.token.AccessTokenRepository;
import com.paran.sample.persistence.repository.user.UserRepository;
import com.paran.sample.persistence.type.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public RegisterRes register(RegisterReq req) {
        var password = passwordEncoder.encode(req.password());
        var user = req.toUserEntity(password);
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveAccessToken(savedUser, jwtToken);
        return new RegisterRes(savedUser.getUserIdx(), jwtToken, refreshToken);
    }

    @Override
    public LoginRes login(LoginReq req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.loginId(),
                        req.password()
                )
        );
        var user = userRepository.findByLoginId(req.loginId())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllAccessTokens(user);
        saveAccessToken(user, jwtToken);
        return new LoginRes(user.getUserIdx(), user.getLoginId(), user.getName(), jwtToken, refreshToken);
    }

    private void saveAccessToken(User user, String jwtToken) {
        var accessToken = AccessToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        accessTokenRepository.save(accessToken);
    }

//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = accessTokenRepository.findAllValidTokenByUser(user.getLoginId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }

}
