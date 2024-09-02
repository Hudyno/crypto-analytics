package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.mapper.enums.StatusMapper;
import com.crypto.cryptocompare.api.data.response.ExchangeData;
import com.crypto.persistence.model.Exchange;
import com.crypto.persistence.model.ExchangeType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        uses = StatusMapper.class)
public abstract class ExchangeMapper {

    @Mapping(target = "status", source = "data.exchangeStatus", qualifiedByName = StatusMapper.FROM_STRING)
    @Mapping(target = "exchangeType", source = "type")
    public abstract Exchange fromExchangeData(ExchangeData data, ExchangeType type);

}
