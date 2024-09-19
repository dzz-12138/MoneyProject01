package com.dzz.stock.mapper;

import com.dzz.stock.pojo.entity.StockBusiness;

/**
* @author dzz
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-09-16 19:13:55
* @Entity com.dzz.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

}
