package com.crypto.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class TimeClosingPriceDto {

    private ZonedDateTime time;

    private Double close;
}
