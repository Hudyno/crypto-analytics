package com.crypto.analytics.mapper.enums;

import com.crypto.cryptocompare.api.data.response.AssetIndustry;
import com.crypto.cryptocompare.api.data.response.AssetIndustryItem;
import com.crypto.persistence.model.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class SectorMapper {

    public static final String FROM_ASSET_INDUSTRY = "fromAssetIndustry";
    public static final String FROM_ASSET_INDUSTRY_LIST = "fromAssetIndustryList";

    @Named(FROM_ASSET_INDUSTRY)
    @ValueMapping(target = "CURRENCIES", source = "PAYMENT")
    @ValueMapping(target = "CURRENCIES", source = "STABLECOIN")
    @ValueMapping(target = "SMART_CONTRACT_PLATFORMS", source = "PLATFORM")
    @ValueMapping(target = "FINANCIALS", source = "CROSS_CHAIN_INFRASTRUCTURE")
    @ValueMapping(target = "FINANCIALS", source = "DECENTRALIZED_FINANCE_PROTOCOL")
    @ValueMapping(target = "FINANCIALS", source = "YIELD_FARMING")
    @ValueMapping(target = "FINANCIALS", source = "LIQUID_STAKED")
    @ValueMapping(target = "FINANCIALS", source = "WRAPPED_COLLATERAL")
    @ValueMapping(target = "CONSUMER_AND_CULTURE", source = "MEME")
    @ValueMapping(target = "CONSUMER_AND_CULTURE", source = "GAMING")
    @ValueMapping(target = "UTILITIES_AND_SERVICES", source = "IDENTITY")
    @ValueMapping(target = "UTILITIES_AND_SERVICES", source = "MEMBERSHIP")
    @ValueMapping(target = "UTILITIES_AND_SERVICES", source = "COLLECTIBLE")
    @ValueMapping(target = "UTILITIES_AND_SERVICES", source = "REPUTATION")
    @ValueMapping(target = "UTILITIES_AND_SERVICES", source = "GOVERNANCE")
    public abstract Sector fromAssetIndustry(AssetIndustry industry);

    @Named(FROM_ASSET_INDUSTRY_LIST)
    public Set<Sector> fromAssetIndustryList(List<AssetIndustryItem> industries) {
        if (CollectionUtils.isEmpty(industries)) {
            return Collections.emptySet();
        }

        return industries.stream()
                         .map(it -> fromAssetIndustry(it.getAssetIndustry()))
                         .collect(Collectors.toSet());
    }
}
