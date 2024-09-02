package com.crypto.analytics.mapper.enums;

import com.crypto.persistence.model.IntervalType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public abstract class IntervalTypeMapper {

    public static final String FROM_STRING = "fromString";

    @Named(FROM_STRING)
    @ValueMapping(target = "DAILY", source = "DAY")
    public abstract IntervalType fromString(String value);
}
