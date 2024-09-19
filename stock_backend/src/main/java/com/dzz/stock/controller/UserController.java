package com.dzz.stock.controller;

import com.dzz.stock.pojo.entity.SysUser;
import com.dzz.stock.service.UserService;
import com.dzz.stock.vo.req.LoginReqVo;
import com.dzz.stock.vo.resp.LoginRespVo;
import com.dzz.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "用户相关接口处理器")
public class UserController {
    @Autowired
    private UserService userService;

    //根据用户名查询用户信息
    @ApiOperation(value = "根据用户名查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "用户名",dataType = "string",required = true,type = "path")
    })
    @GetMapping("/user/{username}")
    public SysUser getUserByUserName(@PathVariable("username") String name){
        return userService.findByUserName(name);
    }

    //登录功能
    @ApiOperation(value = "用户登录功能")

    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);
    }

    //生成图片验证码功能
    @ApiOperation(value = "生成图片验证码功能")
    @GetMapping("/captcha")
    public R<Map> getCapchaCode(){
        return userService.getCapchaCode();
    }
}
