package com.dzz.stock.service.impl;

import com.dzz.stock.mapper.SysUserMapper;
import com.dzz.stock.pojo.entity.SysUser;
import com.dzz.stock.service.UserService;
import com.dzz.stock.vo.req.LoginReqVo;
import com.dzz.stock.vo.resp.LoginRespVo;
import com.dzz.stock.vo.resp.R;
import com.dzz.stock.vo.resp.ResponseCode;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//定义用户服务实现
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //根据用户名查询用户信息
    @Override
    public SysUser findByUserName(String username) {
        return sysUserMapper.findUserInfoByUserName(username);
    }
    //用户登录
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //1.判断参数是否合法
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.ERROR);
        }
        //根据用户名查询用户信息
        SysUser dbUser = sysUserMapper.findUserInfoByUserName(vo.getUsername());
        if(dbUser == null){
            //返回账户不存在
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //3.根据密码匹配器查询输入的密码是否正确
        if(!passwordEncoder.matches(vo.getPassword(),dbUser.getPassword())){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //4.响应前端
        LoginRespVo loginRespVo = new LoginRespVo();
        BeanUtils.copyProperties(dbUser,loginRespVo);
        return R.ok(loginRespVo);
    }
}
