package com.paran.sample.dto.auth;

public record RegisterRes(Long userIdx, String accessToken, String refreshToken) {
}
