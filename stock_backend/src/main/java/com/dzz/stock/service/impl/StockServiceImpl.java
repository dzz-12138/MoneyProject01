package com.dzz.stock.service.impl;/*
 *
 *创建日期:2024-10-08
 */

import com.dzz.stock.mapper.StockMarketIndexInfoMapper;
import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.pojo.vo.StockInfoConfig;
import com.dzz.stock.service.StockService;
import com.dzz.stock.vo.resp.R;
import com.dzz.stock.utils.DateTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    /**
     * 获取国内大盘的实时数据
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> innerIndexAll() {
        //1.获取国内A股大盘的id集合(获取大盘编码集合)
        List<String> mCodes = stockInfoConfig.getInner();
        //2.获取最近股票交易日期
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //先做个假数据，等写完数据采集
        //Date curdate = curDateTime.toDate();
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        curDate = DateTime.parse("2022-12-19 15:00:00",
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //3.调用mapper查询数据
        List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(curDate,mCodes);
        //4.返回查询结果
        return R.ok(data);
    }
}
