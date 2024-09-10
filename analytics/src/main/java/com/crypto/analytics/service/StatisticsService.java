package com.crypto.analytics.service;

import com.crypto.analytics.model.DescriptiveStatisticsDto;
import com.crypto.analytics.model.StatisticalRelationshipDto;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    public static DescriptiveStatisticsDto calculateDescriptiveStatistics(List<Double> closingPrices) {

        double[] closingPricesArr = closingPrices.stream().mapToDouble(Double::doubleValue).toArray();

        org.apache.commons.math3.stat.descriptive.DescriptiveStatistics descriptiveStatistics = new org.apache.commons.math3.stat.descriptive.DescriptiveStatistics(closingPricesArr);

        return new DescriptiveStatisticsDto(
                descriptiveStatistics.getMean(), new Median().evaluate(closingPricesArr), descriptiveStatistics.getStandardDeviation(),
                descriptiveStatistics.getKurtosis(), descriptiveStatistics.getSkewness(), descriptiveStatistics.getMin(),
                descriptiveStatistics.getMax(), descriptiveStatistics.getN()
        );
    }
}
