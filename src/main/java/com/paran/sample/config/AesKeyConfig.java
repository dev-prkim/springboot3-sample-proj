package com.paran.sample.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AesKeyConfig {
    private static final String jpaDbKey = "A0mASftYWeA9qEqk";

    public String getJpaDbKey() { return jpaDbKey; }
}
