package com.dzz.stock.mapper;

import com.dzz.stock.pojo.domain.StockUpdownDomain;
import com.dzz.stock.pojo.entity.StockRtInfo;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author dzz
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-09-16 19:13:55
* @Entity com.dzz.stock.pojo.entity.StockRtInfo
*/
@ApiModel(description = "针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper")
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 查看指定时间点下的股票数据集合
     * @param curDate 日期时间
     * @return
     */
    List<StockUpdownDomain> getStockInfoByTime(@Param("curDate") Date curDate);

    /**
     * 指定日期范围内股票涨停的数量流水
     * @param startDate 开始时间，一般指开盘时间
     * @param endDate 截止时间
     * @param flag 约定：1代表统计涨停 0：跌停
     * @return
     */
    List<Map> getStockUpDownCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flag") int flag);

    /**
     * 统计指定时间点下股票（A股）在各个涨幅区间的数量
     * @param curDate
     * @return
     */
    List<Map> getIncreaseRangInfoByDate(@Param("datatime") Date curDate);
}
