package com.dzz.stock.config;

import com.dzz.stock.pojo.vo.StockInfoConfig;
import com.dzz.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description 定义公共配置类
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class})
public class CommonConfig {
    /**
     * 密码加密器
     * 定义密码加密，匹配器bean
     * BCryptPasswordEncoder方法采用SHA-256对密码进行加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /*
    * 基于雪花算法保证生成的sessionID唯一
    */
    @Bean
    public IdWorker idWorker(){
        /*
        * 参数一；机器id
        * 参数二：机房id
        * 机器与机房编号一般由运维人员进行唯一性规划
        * */
        return new IdWorker(1l,2l);
    }
}