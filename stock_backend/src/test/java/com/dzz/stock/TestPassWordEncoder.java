package com.dzz.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class TestPassWordEncoder {

    @Autowired
    private PasswordEncoder passwordEncoder;
    /*
    测试密码加密
    */
    @Test
    public void test01(){
        String pwd = "123456";
        String encode = passwordEncoder.encode(pwd);
        System.out.println(encode);

    }
}
