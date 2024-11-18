package com.dzz.stock.service;/*
 *
 *创建日期:2024-10-08
 */

import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.pojo.domain.StockUpdownDomain;
import com.dzz.stock.vo.resp.PageResult;
import com.dzz.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface StockService {

    R<List<InnerMarketDomain>> innerIndexAll();

    R<PageResult<StockUpdownDomain>> getStockInforByPage(Integer page, Integer pageSize);

    /**
     * 统计最近股票交易日期
     * @return
     */
    R<Map<String, List>> getStockUpDownCount();

    /**
     * 导出指定页码的最新股票信息
     * @param page 当前页
     * @param pageSize 每页大小
     * @param response
     */
    void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response);

    /**
     * 统计大盘 T日和 T-1日每分钟交易量的统计
     * @return
     */
    R<Map<String, List>> getCompareStockTradeAmt();

    /**
     * 统计最新交易时间点下股票（A股）在各个涨幅区间的数量
     * @return
     */
    R<Map> getIncreaseRangInfo();
}
