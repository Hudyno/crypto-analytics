package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.mapper.enums.StatusMapper;
import com.crypto.cryptocompare.api.data.response.InstrumentData;
import com.crypto.persistence.model.Pair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        uses = { StatusMapper.class })
public abstract class PairMapper {

    @Mapping(target = "baseSymbol", source = "data.instrumentMapping.base")
    @Mapping(target = "quoteSymbol", source = "data.instrumentMapping.quote")
    @Mapping(target = "status", source = "data.instrumentStatus", qualifiedByName = StatusMapper.FROM_STRING)
    public abstract Pair fromInstrumentData(InstrumentData data);
}
