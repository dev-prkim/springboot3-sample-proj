package com.paran.sample.domain.auth.dto;

public record RegisterRes(Long userIdx, String accessToken, String refreshToken) {
}
