package com.crypto.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CoinClosingPricesDto {

    private String symbol;

    private List<TimeClosingPriceDto> timeClosingPrice;

    public List<Double> getClosingPrices() {
        return this.getTimeClosingPrice().stream()
                                         .map(TimeClosingPriceDto::getClose)
                                         .toList();
    }
}
