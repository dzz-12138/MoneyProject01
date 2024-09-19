package com.dzz.stock.pojo.vo;

/*
 *
 *创建日期:2024-10-07
 定义股票相关的值对象封装
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@ConfigurationProperties(prefix = "stock")
//@Component
public class StockInfoConfig {
    //封装国内A股大盘密码集
    private List<String> inner;

    //外盘编码集合
    private List<String> outer;
}
