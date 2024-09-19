package com.dzz.stock.controller;

import com.dzz.stock.pojo.entity.SysUser;
import com.dzz.stock.service.UserService;
import com.dzz.stock.vo.req.LoginReqVo;
import com.dzz.stock.vo.resp.LoginRespVo;
import com.dzz.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    //根据用户名查询用户信息
    @GetMapping("/user/{username}")
    public SysUser getUserByUserName(@PathVariable("username") String name){
        return userService.findByUserName(name);
    }

    //登录功能
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);
    }
}
