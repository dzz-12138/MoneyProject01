package com.dzz.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.dzz.stock.constant.StockConstant;
import com.dzz.stock.mapper.SysUserMapper;
import com.dzz.stock.pojo.entity.SysUser;
import com.dzz.stock.service.UserService;
import com.dzz.stock.utils.IdWorker;
import com.dzz.stock.vo.req.LoginReqVo;
import com.dzz.stock.vo.resp.LoginRespVo;
import com.dzz.stock.vo.resp.R;
import com.dzz.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//定义用户服务实现
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;

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
        //2.判断输入的的验证码是否存在
        if(StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())){
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //判断redis里面保存的验证码是否与输入的验证码一致（比较时忽略大小写）
        //取出系统生成的验证码
        String redisCode = (String) redisTemplate.opsForValue()
                .get(StockConstant.CHECK_PREFIX + vo.getSessionId());//获取系统生成的验证码
        if(StringUtils.isBlank(redisCode)){
            //验证码过期，不存在
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        //判断验证码是否正确
        if(! redisCode.equalsIgnoreCase(vo.getCode())){
            return R.error(ResponseCode.CHECK_CODE_ERROR);
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

    //生成图片验证码功能
    @Override
    public R<Map> getCapchaCode() {
        //1.生成图片验证码
        /**
         * 参数一：图片的宽度
         * 参数二：图片高度
         * 参数三：图片中包含验证码的长度
         * 参数四：干扰线数量
         */

        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);

        captcha.setBackground(Color.blue);
        //获取校验码
        String checkCode = captcha.getCode();
        //获取经过base64编码处理的图片数据
        String imageData = captcha.getImageBase64();
        //生成sessionId 转化成String，避免前端精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的图片校验码：{}，会话id：{}",checkCode,sessionId);
        //将sessionId作为key，校验码作为value配存在redis中
        /*
        使用redis模仿session的行为，通过过期时间的设置
         */
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX + sessionId,checkCode,5, TimeUnit.MINUTES);
        //组装数据
        Map<String,String> data = new HashMap();
        data.put("imageData",imageData);
        data.put("sessionId",sessionId);
        //响应数据
        return R.ok(data);
    }
}
