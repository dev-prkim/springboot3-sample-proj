package com.paran.sample.domain.auth.dto;

public record LoginRes(Long userIdx, String loginId, String name, String accessToken, String refreshToken) {
}
