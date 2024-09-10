package com.crypto.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PairLightDto {

    private String baseSymbol;
    private String quoteSymbol;
    private String exchange;
}
