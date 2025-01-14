package com.green.jwt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt-const") //jwt-const --> yaml파일안에 있는 이름과 같다.
@RequiredArgsConstructor
@ToString
public class JwtConst {
    private final String issuer; //멤버필드명은 yaml에 있는 이름과 맞춰주면 된다.
    private final String secret;
    private final String headerSchemaName;
    private final String tokenType;
    private final String tokenName;
    private final String claimKey;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;
    private final String refreshTokenCookieName;
    private final int refreshTokenCookieExpire;
}