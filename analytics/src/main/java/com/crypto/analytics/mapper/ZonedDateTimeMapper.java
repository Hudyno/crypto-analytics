package com.crypto.analytics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public abstract class ZonedDateTimeMapper {

    public static final String FROM_UNIX_TIMESTAMP = "fromUnixTimestamp";

    @Named(FROM_UNIX_TIMESTAMP)
    public ZonedDateTime fromUnixTimestamp(Long timestamp) {
        if (isNull(timestamp)) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp).atZone(ZoneId.of("UTC"));
    }
}
