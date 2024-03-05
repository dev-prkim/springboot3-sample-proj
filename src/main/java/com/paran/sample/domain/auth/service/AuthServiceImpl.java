package com.paran.sample.domain.auth.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RegisterRes register(RegisterReq req) {
        validateLoginIdFormat(req.loginId());
        validateLoginIdDuplication(req.loginId());

        var password = passwordEncoder.encode(req.password());
        var user = req.toUserEntity(password);
        var savedUser = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveAccessToken(savedUser, jwtToken, refreshToken);

        return new RegisterRes(savedUser.getAppUserIdx(), jwtToken, refreshToken);
    }

    @Transactional
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
        revokeAllAccessTokens(user);
        saveAccessToken(user, jwtToken, refreshToken);
        return new LoginRes(user.getAppUserIdx(), user.getLoginId(), user.getName(), jwtToken, refreshToken);
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
        var validUserTokens = user.getTokens();
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        accessTokenRepository.saveAll(validUserTokens);
    }

}
