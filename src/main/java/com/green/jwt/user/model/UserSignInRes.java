package com.green.jwt.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInRes {
    private long id;
    private String name;
    private String accessToken;
}