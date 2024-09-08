package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.mapper.ZonedDateTimeMapper;
import com.crypto.analytics.mapper.enums.StatusMapper;
import com.crypto.cryptocompare.api.data.response.CexInstrumentData;
import com.crypto.cryptocompare.api.data.response.DexInstrumentData;
import com.crypto.cryptocompare.api.data.response.InstrumentData;
import com.crypto.persistence.model.Pair;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring",
        uses = { StatusMapper.class })
public abstract class PairMapper {

    @Autowired
    private ZonedDateTimeMapper zonedDateTimeMapper;

    @Mapping(target = "baseSymbol", source = "data.instrumentMapping.base")
    @Mapping(target = "quoteSymbol", source = "data.instrumentMapping.quote")
    @Mapping(target = "status", source = "data.instrumentStatus", qualifiedByName = StatusMapper.FROM_STRING)
    public abstract Pair fromInstrumentData(InstrumentData data);


    @AfterMapping
    protected void afterMapping(@MappingTarget Pair pair, InstrumentData data) {
        if (data instanceof CexInstrumentData cexData) {
            pair.setFirstTradeTime(zonedDateTimeMapper.fromUnixTimestamp(cexData.getFirstTradeSpotTimestamp()));
            pair.setLastTradeTime(zonedDateTimeMapper.fromUnixTimestamp(cexData.getLastTradeSpotTimestamp()));
            pair.setTotalTrades(cexData.getTotalTradesSpot());
        }
        if (data instanceof DexInstrumentData dexData) {
            pair.setFirstTradeTime(zonedDateTimeMapper.fromUnixTimestamp(dexData.getFirstAmmSwapOnChainTimestamp()));
            pair.setLastTradeTime(zonedDateTimeMapper.fromUnixTimestamp(dexData.getLastAmmSwapOnChainTimestamp()));
            pair.setTotalTrades(dexData.getTotalAmmSwapsOnChain());
        }
    }
}
