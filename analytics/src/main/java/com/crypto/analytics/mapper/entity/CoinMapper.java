package com.crypto.analytics.mapper.entity;

import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.persistence.model.Coin;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class CoinMapper {

    public abstract Coin fromAssetSummary(AssetSummary summary);
}
