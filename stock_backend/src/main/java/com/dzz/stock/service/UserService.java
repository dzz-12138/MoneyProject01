package com.dzz.stock.service;

import com.dzz.stock.pojo.entity.SysUser;
import com.dzz.stock.vo.req.LoginReqVo;
import com.dzz.stock.vo.resp.LoginRespVo;
import com.dzz.stock.vo.resp.R;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserService {
    //根据用户名查询用户信息
    SysUser findByUserName(String username);

    R<LoginRespVo> login(LoginReqVo vo);

    R<Map> getCapchaCode();
}
