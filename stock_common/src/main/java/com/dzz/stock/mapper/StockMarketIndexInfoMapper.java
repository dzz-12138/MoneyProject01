package com.dzz.stock.mapper;

import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author dzz
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-09-16 19:13:55
* @Entity com.dzz.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    /*
    * @param curDate：指定时间点
    * @param mCodes：大盘编码集合
    *
    */
    List<InnerMarketDomain> getMarketInfo(@Param("curDate") Date curDate, @Param("mCodes") List<String> mCodes);

    /**
     * 统计指定日期范围内大盘每分钟的成交量流水线信息
     * @param openDate 起始时间，一般指开盘时间
     * @param endDate 截止时间，一般与openDate同一天
     * @param marketCodes 大盘编码集合
     * @return
     */
    List<Map> getSumAmtInfo(@Param("openDate") Date openDate, @Param("endDate") Date endDate, @Param("marketCodes") List<String> marketCodes);
}
