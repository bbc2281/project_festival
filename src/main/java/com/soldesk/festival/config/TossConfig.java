package com.soldesk.festival.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class TossConfig {

    @Value("${toss.clientKey}")
    private String clientKey;

    @Value("${toss.secretKey}")
    private String secretKey;

    @Value("${toss.successUrl}")
    private String successUrl;

    @Value("${toss.failUrl}")
    private String failUrl;


}
