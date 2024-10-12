package com.crypto.analytics.mapper.enums;

import com.crypto.persistence.model.MarketCapTier;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public abstract class MarketCapTierMapper {

    public static final String FROM_TOTAL_MARKET_CAP = "fromTotalMarketCap";

    @Named(FROM_TOTAL_MARKET_CAP)
    public MarketCapTier fromTotalMarketCap(BigDecimal marketCap) {

        if (marketCap.compareTo(new BigDecimal(10000000000L)) > 0) {
            return MarketCapTier.LARGE_CAP;
        }
        else if (marketCap.compareTo(new BigDecimal(1000000000L)) > 0) {
            return MarketCapTier.MID_CAP;
        }
        else if (marketCap.compareTo(new BigDecimal(50000000)) > 0) {
            return MarketCapTier.SMALL_CAP;
        }
        else {
            return MarketCapTier.MICRO_CAP;
        }
    }
}
