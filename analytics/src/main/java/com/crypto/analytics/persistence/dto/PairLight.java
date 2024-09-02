package com.crypto.analytics.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PairLight {

    private String baseSymbol;
    private String quoteSymbol;
    private String exchange;
}
