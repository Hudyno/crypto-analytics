package com.crypto.analytics.mapper.entity;

import com.crypto.cryptocompare.api.data.response.AssetTopListData;
import com.crypto.persistence.model.CoinMetrics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CoinMetricsMapper {

    public abstract CoinMetrics fromAssetTopListData(AssetTopListData data);
}
