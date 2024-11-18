package com.dzz.stock.controller;/*
 *
 *创建日期:2024-10-08
 定义股票相关接口控制器
 */

import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.pojo.domain.StockUpdownDomain;
import com.dzz.stock.service.StockService;
import com.dzz.stock.vo.resp.PageResult;
import com.dzz.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/*
* 定义股票相关接口控制类
* */
@Api(value = "/api/quot", tags = {"定义股票相关接口控制类 */"})
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
    /**
     * 分页查询最新的股票交易数据
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页大小")
    })
    @ApiOperation(value = "分页查询最新的股票交易数据", notes = "分页查询最新的股票交易数据", httpMethod = "GET")
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockInforByPage(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                                                @RequestParam(value = "page",required = false,defaultValue = "20")Integer pageSize){
        return stockService.getStockInforByPage(page,pageSize);
    }

    /**
     * 统计最近股票交易日内每分钟的涨跌停的股票数量
     * @return
     */
    @ApiOperation(value = "统计最近股票交易日内每分钟的涨跌停的股票数量", notes = "统计最近股票交易日内每分钟的涨跌停的股票数量", httpMethod = "GET")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpDownCount(){
        return stockService.getStockUpDownCount();
    }

    /**
     * 导出指定页码的最新股票信息
     * @param page 当前页
     * @param pageSize 每页大小
     * @param response
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页大小")
    })
    @ApiOperation(value = "导出指定页码的最新股票信息", notes = "导出指定页码的最新股票信息", httpMethod = "GET")
    @GetMapping("/stock/export")
    public void exportStockUpDownInfo(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                    @RequestParam(value = "page",required = false,defaultValue = "20")Integer pageSize,
                                    HttpServletResponse response){
         stockService.exportStockUpDownInfo(page,pageSize,response);
    }

    /**
     * 统计大盘 T日和 T-1日每分钟交易量的统计
     * @return
     */
    @ApiOperation(value = "统计大盘 T日和 T-1日每分钟交易量的统计",
            notes = "统计大盘 T日和 T-1日每分钟交易量的统计", httpMethod = "GET")
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> getCompareStockTradeAmt(){
        return stockService.getCompareStockTradeAmt();
    }

    /**
     * 统计最新交易时间点下股票（A股）在各个涨幅区间的数量
     * @return
     */
    @GetMapping("/stock/updown")
    public R<Map> getIncreaseRangInfo(){
        return stockService.getIncreaseRangInfo();
    }
}
