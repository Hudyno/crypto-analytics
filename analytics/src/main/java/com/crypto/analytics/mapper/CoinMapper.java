package com.crypto.analytics.mapper;

import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.persistence.model.Coin;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CoinMapper {

    public abstract Coin fromAssetSummary(AssetSummary assetSummary);

    public abstract List<Coin> fromAssetSummary(List<AssetSummary> assetSummaryList);
}
