package com.paran.sample.dto.auth;

public record LoginRes(Long userIdx, String email, String name, String accessToken, String refreshToken) {
}
