package com.crypto.persistence.mapper.dto;

import com.crypto.persistence.dto.PairExchangeDetailDto;
import com.crypto.persistence.projection.PairExchangeProjection;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PairExchangeDetailDtoMapper {

    public static final String FROM_PAIR_EXCHANGE_PROJECTION = "fromPairExchangeProjection";
    public static final String FROM_PAIR_EXCHANGE_PROJECTION_LIST = "fromPairExchangeProjectionList";

    @Named(FROM_PAIR_EXCHANGE_PROJECTION)
    @Mapping(target = "exchange", source = "exchangeName")
    public abstract PairExchangeDetailDto fromPairExchangeProjection(PairExchangeProjection projection);

    @Named(FROM_PAIR_EXCHANGE_PROJECTION_LIST)
    @IterableMapping(qualifiedByName = FROM_PAIR_EXCHANGE_PROJECTION)
    public abstract List<PairExchangeDetailDto> fromPairExchangeProjectionList(List<PairExchangeProjection> projections);
}
