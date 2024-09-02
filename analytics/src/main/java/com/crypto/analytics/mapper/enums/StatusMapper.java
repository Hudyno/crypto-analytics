package com.crypto.analytics.mapper.enums;

import com.crypto.persistence.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public abstract class StatusMapper {

    public static final String FROM_STRING = "fromString";

    @Named(FROM_STRING)
    @ValueMapping(target = "ACTIVE", source = "ACTIVE")
    @ValueMapping(target = "OTHER", source = MappingConstants.ANY_UNMAPPED)
    public abstract Status fromString(String value);
}
