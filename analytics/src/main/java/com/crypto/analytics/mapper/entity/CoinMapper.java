package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.mapper.ZonedDateTimeMapper;
import com.crypto.analytics.mapper.enums.MarketCapTierMapper;
import com.crypto.analytics.mapper.enums.SectorMapper;
import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.cryptocompare.api.data.response.AssetTopListData;
import com.crypto.persistence.model.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring",
        uses = { ZonedDateTimeMapper.class, SectorMapper.class, MarketCapTierMapper.class }
)
public abstract class CoinMapper {

    @Mapping(target = "launchDate", source = "launchDate", qualifiedByName = ZonedDateTimeMapper.FROM_UNIX_TIMESTAMP)
    public abstract Coin fromAssetSummary(AssetSummary summary);

    @Mapping(target = "createdOn", source = "createdOn", qualifiedByName = ZonedDateTimeMapper.FROM_UNIX_TIMESTAMP)
    @Mapping(target = "updatedOn", source = "updatedOn", qualifiedByName = ZonedDateTimeMapper.FROM_UNIX_TIMESTAMP)
    @Mapping(target = "launchDate", source = "launchDate", qualifiedByName = ZonedDateTimeMapper.FROM_UNIX_TIMESTAMP)
    @Mapping(target = "sectors", source = "industries", qualifiedByName = SectorMapper.FROM_ASSET_INDUSTRY_LIST)
    @Mapping(target = "tier", source = "totalMktCapUsd", qualifiedByName = MarketCapTierMapper.FROM_TOTAL_MARKET_CAP)
    public abstract void updateFromAssetTopListData(@MappingTarget Coin coin, AssetTopListData data);
}
