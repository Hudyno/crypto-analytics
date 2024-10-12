package com.crypto.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class TimeCirculatingSupplyDto {

    private ZonedDateTime time;

    private BigDecimal supplyCirculating;
}
