package com.crypto.analytics.util;

import com.crypto.analytics.model.DescriptiveStatisticsDto;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsUtil {

    public static DescriptiveStatisticsDto calculateDescriptiveStatistics(List<Double> closingPrices) {

        double[] prices = closingPrices.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        org.apache.commons.math3.stat.descriptive.DescriptiveStatistics descriptiveStatistics = new org.apache.commons.math3.stat.descriptive.DescriptiveStatistics(prices);

        return new DescriptiveStatisticsDto(
                descriptiveStatistics.getMean(), new Median().evaluate(prices), descriptiveStatistics.getStandardDeviation(),
                descriptiveStatistics.getKurtosis(), descriptiveStatistics.getSkewness(), descriptiveStatistics.getMin(),
                descriptiveStatistics.getMax(), descriptiveStatistics.getN()
        );
    }
}
