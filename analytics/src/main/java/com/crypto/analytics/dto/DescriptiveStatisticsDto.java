package com.crypto.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DescriptiveStatisticsDto {

    private Double mean;

    private Double median;

    private Double standardDeviation;

    private Double kurtosis;

    private Double skewness;

    private Double minimum;

    private Double maximum;

    private Long count;

    private Double dailyVolatility;

    private Double weeklyVolatility;

    private Double monthlyVolatility;

    private Double yearlyVolatility;
}
