package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.mapper.ZonedDateTimeMapper;
import com.crypto.cryptocompare.api.data.response.HistoricalSupply;
import com.crypto.persistence.model.HistoricalSupplyEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        uses = { ZonedDateTimeMapper.class })
public abstract class HistoricalSupplyEntryMapper {

    @Mapping(target = "time", source = "timestamp", qualifiedByName = ZonedDateTimeMapper.FROM_UNIX_TIMESTAMP)
    public abstract HistoricalSupplyEntry fromHistoricalSupply(HistoricalSupply supply);
}
