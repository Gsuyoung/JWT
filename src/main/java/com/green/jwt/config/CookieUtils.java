package com.green.jwt.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class CookieUtils {
    //Req header에서 내가 원하는 쿠키를 찾는 메소드
    public Cookie getCookie(HttpServletRequest req, String name) {
        //Optional.ofNullable 메소드는 null을 가질 수 있는 옵셔널을 생성
        Optional<Cookie[]> optCookie = Optional.ofNullable(req.getCookies());
        return Arrays.stream(optCookie.orElseThrow(() -> new RuntimeException("Cookie not found")))//Stream<Cookie[]> 생성
                .filter(item -> item.getName().equals(name))//Predicate 인터페이스(boolean 타입, 파라미터 있음) 필터 조건에 맞는 Stream을 리턴 (중간 연산)
                .findFirst() //첫번째 아이템 선택해서 Optional로 리턴 (최종연산) --> stream이 아니라면 최종연산
                .orElseThrow(() -> new RuntimeException("Cookie not found"));
    }

    //Res header에 내가 원하는 쿠키를 담는 메소드
    public void setCookie(HttpServletResponse res, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/api/user/access-token"); //이 요청으로 들어올 때만 쿠키가 넘어올 수 있도록
        cookie.setHttpOnly(true); //보안 쿠키 설정, 프론트에서 JS로 쿠키값을 얻을 수 없다.
        cookie.setMaxAge(maxAge);
        res.addCookie(cookie);
    }


}
