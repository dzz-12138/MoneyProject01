package com.dzz.stock.controller;/*
 *
 *创建日期:2024-10-08
 定义股票相关接口控制器
 */

import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.service.StockService;
import com.dzz.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/*
* 定义股票相关接口控制类
* */
@Api(value = "/api/quot", tags = {"定义股票相关接口控制类"})
@RestController
@RequestMapping("/api/quot")
public class StockController {
    @Autowired
    private StockService stockService;


    /**
     * 获取国内最新大盘指数
     * @return
     */
    @ApiOperation(value = "获取国内最新大盘指数", notes = "获取国内最新大盘指数", httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> innerIndexAll(){
        return stockService.innerIndexAll();
    }
}
