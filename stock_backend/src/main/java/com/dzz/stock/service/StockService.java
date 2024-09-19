package com.dzz.stock.service;/*
 *
 *创建日期:2024-10-08
 */

import com.dzz.stock.pojo.domain.InnerMarketDomain;
import com.dzz.stock.vo.resp.R;

import java.util.List;

public interface StockService {

    R<List<InnerMarketDomain>> innerIndexAll();
}
