package com.dzz.stock.mapper;

import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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
}
