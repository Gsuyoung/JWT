package com.green.jwt.user;

import com.green.jwt.user.model.UserSelOne;
import com.green.jwt.user.model.UserSignInReq;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void selUserWithRoles() {
        UserSignInReq req = new UserSignInReq("rndudtn3737@naver.com", "");
        UserSelOne userSelOne = userMapper.selUserWithRoles(req).orElseThrow(() -> {
            throw new RuntimeException("아이디를 확인해 주세요.");
        });
        System.out.println(userSelOne);
    }
}