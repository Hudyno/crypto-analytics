package com.crypto.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Period {
    LAST_60D("60"),
    LAST_120D("120"),
    LAST_365D("365"),
    ALL_TIME(String.valueOf(Integer.MAX_VALUE));

    private final String value;
}
