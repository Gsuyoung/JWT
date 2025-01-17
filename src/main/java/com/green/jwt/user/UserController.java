package com.green.jwt.user;

import com.green.jwt.user.model.UserSignInReq;
import com.green.jwt.user.model.UserSignInRes;
import com.green.jwt.user.model.UserSignUpReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("sign-up")
    public long signUp(@RequestBody UserSignUpReq req) {
        log.info("Sign up request: {}", req);
        userService.signUp(req);
        return req.getId();
    }

    @PostMapping("sign-in")
    public UserSignInRes signIn(@RequestBody UserSignInReq req, HttpServletResponse response) {
        log.info("Sign in request: {}", req);

        return userService.signIn(req, response);
    }
}
