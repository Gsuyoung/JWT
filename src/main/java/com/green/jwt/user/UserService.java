package com.green.jwt.user;

import com.green.jwt.config.CookieUtils;
import com.green.jwt.config.JwtConst;
import com.green.jwt.config.jwt.JwtTokenProvider;
import com.green.jwt.config.jwt.JwtUser;
import com.green.jwt.user.model.UserSelOne;
import com.green.jwt.user.model.UserSignInReq;
import com.green.jwt.user.model.UserSignInRes;
import com.green.jwt.user.model.UserSignUpReq;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final JwtConst jwtConst;

    public void signUp(UserSignUpReq req) {
        String hashedPw = passwordEncoder.encode(req.getPw());
        req.setPw(hashedPw);

        transactionTemplate.execute(status -> {
            userMapper.insUser(req);
            userMapper.insUserRole(req);
            return null;
        });
    }

    public UserSignInRes signIn(UserSignInReq req, HttpServletResponse response) {
        UserSelOne userSelOne = userMapper.selUserWithRoles(req).orElseThrow(() -> {
            throw new RuntimeException("아이디를 확인해 주세요.");
        });

        if(!passwordEncoder.matches(req.getPw(), userSelOne.getPw())) {
            throw new RuntimeException("비밀번호를 확인해 주세요.");
        }

        //AT, RT 발행
        JwtUser jwtUser = new JwtUser(userSelOne.getId(), userSelOne.getRols());

        String accessToken = jwtTokenProvider.generateAccessToken(jwtUser);
        String refreshToken = jwtTokenProvider.generateAccessToken(jwtUser);

        //RT를 쿠기에 담는다.
        cookieUtils.setCookie(response,jwtConst.getRefreshTokenCookieName(), refreshToken, jwtConst.getRefreshTokenCookieExpire());


        return UserSignInRes.builder()
                .accessToken(accessToken)
                .id(userSelOne.getId())
                .name(userSelOne.getName())
                .build();
    }
}