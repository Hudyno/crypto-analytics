package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.mapper.ZonedDateTimeMapper;
import com.crypto.analytics.mapper.enums.IntervalTypeMapper;
import com.crypto.cryptocompare.api.data.response.HistoricalOHLCV;
import com.crypto.persistence.model.PriceTimeSeriesEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        uses = { ZonedDateTimeMapper.class, IntervalTypeMapper.class })
public abstract class PriceTimeSeriesEntryMapper {

    @Mapping(target = "quoteSymbol", source = "quote")
    @Mapping(target = "close", source = "close")
    @Mapping(target = "high", source = "high")
    @Mapping(target = "low", source = "low")
    @Mapping(target = "open", source = "open")
    @Mapping(target = "volume", source = "volume")
    @Mapping(target = "time", source = "timestamp", qualifiedByName = ZonedDateTimeMapper.FROM_UNIX_TIMESTAMP)
    @Mapping(target = "intervalType", source = "unit", qualifiedByName = IntervalTypeMapper.FROM_STRING)
    public abstract PriceTimeSeriesEntry fromHistoricalOHLCV(HistoricalOHLCV historicalOHLCV);
}
