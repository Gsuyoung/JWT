package com.green.jwt.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jwt.config.JwtConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final ObjectMapper objectMapper;
    private final SecretKey secretKey;
    private final JwtConst jwtConst;

    public JwtTokenProvider(ObjectMapper objectMapper, JwtConst jwtConst) {
        this.objectMapper = objectMapper;
        this.jwtConst = jwtConst;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConst.getSecret()));
    }

    public String generateAccessToken(JwtUser jwtUser) {
        return generateToken(jwtUser, jwtConst.getAccessTokenExpiry());
    }

    public String generateRefreshToken(JwtUser jwtUser) {
        return generateToken(jwtUser, jwtConst.getRefreshTokenCookieExpire());
    }

    public String generateToken(JwtUser jwtUser, long tokenValidMillSecond) {
        Date now = new Date();
        return Jwts.builder()
                .header().type(jwtConst.getTokenName())
                .and()
                .issuer(jwtConst.getIssuer())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidMillSecond)) //현재시간부터 15분까지만 사용가능
                .claim(jwtConst.getClaimKey(), makeClaimByUserToString(jwtUser))

                .signWith(secretKey)
                .compact();
    }

    // 객체가 들어갔더니 String으로 변환 : JSON으로 직렬화
    private String makeClaimByUserToString(JwtUser jwtUser) {
        //객체 자체를 JWT에 담고 싶어서 객체를 직렬화하는 과정(객체를 String으로 바꾸는 작업)
        //jwtUser에 담고있는 데이터를 JSON형태의 문자열로 변환
        try {
            return objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //--- 윗부분 토큰 만들어내는 과정
    //--- 만들어진 토큰(AT, RT) 사용
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token) //signWith로 claim을 뽑아내겠다.
                .getPayload();
    }

    public JwtUser getJwtUserFromToken(String token) {
        Claims claims = getClaims(token);
        String json = claims.get(jwtConst.getClaimKey(), String.class);
        try {
           return objectMapper.readValue(json, JwtUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Authentication getAuthentication(String token) {
        JwtUser jwtUser = getJwtUserFromToken(token);
        return new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
    }

}