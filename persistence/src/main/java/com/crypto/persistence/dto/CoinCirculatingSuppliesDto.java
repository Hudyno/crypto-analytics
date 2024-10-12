package com.crypto.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CoinCirculatingSuppliesDto {

    private String symbol;

    private List<TimeCirculatingSupplyDto> timeCirculatingSupply;
}
