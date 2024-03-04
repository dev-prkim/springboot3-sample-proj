package com.paran.sample.domain.auth.dto;

public record LoginRes(Long userIdx, String email, String name, String accessToken, String refreshToken) {
}
