package com.crypto.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class PairExchangeDetailDto {

    private ZonedDateTime firstTradeTime;

    private ZonedDateTime lastTradeTime;

    private Long totalTrades;

    private String exchange;
}
