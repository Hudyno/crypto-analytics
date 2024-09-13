package com.crypto.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PairExchangesDto {

    private String baseSymbol;

    private String quoteSymbol;

    private List<PairExchangeDetailDto> exchangesDetails;
}
