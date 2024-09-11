package com.crypto.analytics.service;

import com.crypto.analytics.dto.DescriptiveStatisticsDto;
import com.crypto.analytics.dto.StatisticalRelationshipDto;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    public static DescriptiveStatisticsDto calculateDescriptiveStatistics(List<Double> closingPrices, Integer periodLength) {

        closingPrices = trimBasedOnPeriod(closingPrices, periodLength);

        double[] closingPricesArr = closingPrices.stream().mapToDouble(Double::doubleValue).toArray();

        org.apache.commons.math3.stat.descriptive.DescriptiveStatistics descriptiveStatistics = new org.apache.commons.math3.stat.descriptive.DescriptiveStatistics(closingPricesArr);

        return new DescriptiveStatisticsDto(
                descriptiveStatistics.getMean(), new Median().evaluate(closingPricesArr), descriptiveStatistics.getStandardDeviation(),
                descriptiveStatistics.getKurtosis(), descriptiveStatistics.getSkewness(), descriptiveStatistics.getMin(),
                descriptiveStatistics.getMax(), descriptiveStatistics.getN()
        );
    }

    public static StatisticalRelationshipDto calculateStatisticalRelationship(List<Double> xClosingPrices, List<Double> yClosingPrices,
                                                                              Integer periodLength) {
        xClosingPrices = trimBasedOnPeriod(xClosingPrices, periodLength);
        yClosingPrices = trimBasedOnPeriod(yClosingPrices, periodLength);

        double[] xClosingPricesArr = xClosingPrices.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yClosingPricesArr = yClosingPrices.stream().mapToDouble(Double::doubleValue).toArray();

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double corr = correlation.correlation(xClosingPricesArr, yClosingPricesArr);
        double beta = calculateBeta(xClosingPricesArr, yClosingPricesArr);

        return new StatisticalRelationshipDto(corr, beta);
    }

    public static double calculateBeta(double[] xClosingPrices, double[] yClosingPrices) {
        Covariance covariance = new Covariance();
        Variance variance = new Variance();

        return covariance.covariance(xClosingPrices, yClosingPrices) / variance.evaluate(xClosingPrices);
    }

    private static List<Double> trimBasedOnPeriod(List<Double> prices, Integer periodLength) {
        if (periodLength != Integer.MAX_VALUE) {
            return prices.subList(Math.max(0, prices.size() - periodLength), prices.size());
        }
        return prices;
    }
}
