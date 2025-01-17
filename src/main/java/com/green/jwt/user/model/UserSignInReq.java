package com.green.jwt.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSignInReq {
    @Schema(title = "이메일", example = "ddd@naver.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(title = "비밀번호", example = "1212", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pw;
}