package com.dzz.stock.service.impl;/*
 *
 *创建日期:2024-10-08
 */

import com.alibaba.excel.EasyExcel;
import com.dzz.stock.mapper.StockMarketIndexInfoMapper;
import com.dzz.stock.mapper.StockRtInfoMapper;
import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.pojo.domain.StockUpdownDomain;
import com.dzz.stock.pojo.vo.StockInfoConfig;
import com.dzz.stock.service.StockService;
import com.dzz.stock.vo.resp.PageResult;
import com.dzz.stock.vo.resp.R;
import com.dzz.stock.utils.DateTimeUtil;
import com.dzz.stock.vo.resp.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
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

    /**
     * 分页查询最新的股票交易数据
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
    @Override
    public R<PageResult<StockUpdownDomain>> getStockInforByPage(Integer page, Integer pageSize) {
        //获取最新的股票交易点
        //获取最近股票交易日期
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //先做个假数据，等写完数据采集
        //Date curdate = curDateTime.toDate();
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        curDate = DateTime.parse("2022-12-30 09:42:00",
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.设置PageHelper分页参数
        PageHelper.startPage(page,pageSize);
        //3.调用mapper查询
        List<StockUpdownDomain> pageDate = stockRtInfoMapper.getStockInfoByTime(curDate);
        //4.组装PageResult对象
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(pageDate);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        //5.响应数据
        return R.ok(pageResult);
    }

    @Override
    public R<Map<String, List>> getStockUpDownCount() {
        //1.获取最新股票的交易时间点（截止时间）
        //1.获取股票最新的交易时间点（截止到分钟，秒和毫秒设置为零）
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //假数据
        curDateTime = DateTime.parse("2022-01-06 14:25:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = curDateTime.toDate();
        //mock data 等后续完成股票采集job工程，在将代码删除
        //2.获取最新的交易时间点对应的开盘时间点
        Date startDate = DateTimeUtil.getOpenDate(curDateTime).toDate();
        //3.统计跌停
        List<Map> upList= stockRtInfoMapper.getStockUpDownCount(startDate,endDate,1);
        //4.统计跌停
        List<Map> downList= stockRtInfoMapper.getStockUpDownCount(startDate,endDate,0);
        //5.组小黄数据
        HashMap<String, List> info = new HashMap<>();
        info.put("upList",upList);
        info.put("downList",downList);
        //6.响应数据
        return R.ok(info);
    }

    /**
     * 导出指定页码的最新股票信息
     * @param page 当前页
     * @param pageSize 每页大小
     * @param response
     */
    @Override
    public void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        //1.获取分页数据
        R<PageResult<StockUpdownDomain>> stockInforByPage = this.getStockInforByPage(page, pageSize);
        List<StockUpdownDomain> rows = stockInforByPage.getData().getRows();

        //2.将数据导入到excel表
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");//告诉浏览器，后端发送的文件格式是excel格式
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("股票信息表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨幅信息").doWrite(rows);
        } catch (IOException e) {
            log.error("当前页码：{}，每页大小：{}，当前时间：{}，异常信息：{}",page,pageSize,DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),e.getMessage());
            //通知前端异常，稍后重试
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> error = R.error(ResponseCode.ERROR);

            try {
                String JsonDate = new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(JsonDate);
            } catch (IOException ex) {
                log.error("当前页码：{}，每页大小：{}，当前时间：{}",page,pageSize,DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }

        }
    }

    /**
     * 统计大盘 T日和 T-1日每分钟交易量的统计
     * @return
     */
    @Override
    public R<Map<String, List>> getCompareStockTradeAmt() {
        //1.获取T日（最新股票交易日的日期范围）
        DateTime tEndDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //mock data
        tEndDateTime = DateTime.parse("2022-01-03 14:40:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tEndDate = tEndDateTime.toDate();
        //开盘时间
        Date tStartDate = DateTimeUtil.getOpenDate(tEndDateTime).toDate();
        //2.获取T-1日的日期范围
        DateTime preTEndDateTime = DateTimeUtil.getPreviousTradingDay(tEndDateTime);
        preTEndDateTime = DateTime.parse("2022-01-02 14:40:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date pretEndDate = preTEndDateTime.toDate();
        //开盘时间
        Date tPreStartDate = DateTimeUtil.getOpenDate(preTEndDateTime).toDate();
        //3.调用mapper查询
        //3.1 统计T日
        List<Map> tData = stockMarketIndexInfoMapper.getSumAmtInfo(tStartDate,tEndDate,stockInfoConfig.getInner());
        //3.2 统计T-1日
        List<Map> preTData = stockMarketIndexInfoMapper.getSumAmtInfo(tPreStartDate,pretEndDate,stockInfoConfig.getInner());
        //4.组装数据
        HashMap<String, List> info = new HashMap<>();
        info.put("amtList",tData);
        info.put("yesAmtList",preTData);
        //5.响应数据
        return R.ok(info);
    }

    /**
     * 统计最新交易时间点下股票（A股）在各个涨幅区间的数量
     * @return
     */
    @Override
    public R<Map> getIncreaseRangInfo() {
        //1.获取最新的股票交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //mock data
        curDateTime = DateTime.parse("2022-01-06 09:55:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = curDateTime.toDate();
        //2.调用Mapper获取数据
        List<Map> infos = stockRtInfoMapper.getIncreaseRangInfoByDate(curDate);
        //3.组装数据
        HashMap<String, Object> data = new HashMap<>();
        data.put("curtime",curDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos",infos);
        //4.响应数据
        return R.ok(data);
    }
}
