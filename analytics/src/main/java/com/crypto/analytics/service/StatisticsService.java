package com.crypto.analytics.service;

import com.crypto.analytics.dto.DescriptiveStatisticsDto;
import com.crypto.analytics.dto.StatisticalRelationshipDto;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {

    public static Optional<DescriptiveStatisticsDto> calculateDescriptiveStatistics(List<Double> closingPrices, Integer periodLength) {

        closingPrices = trimBasedOnPeriod(closingPrices, periodLength);

        double[] closingPricesArr = closingPrices.stream().mapToDouble(Double::doubleValue).toArray();

        org.apache.commons.math3.stat.descriptive.DescriptiveStatistics descriptiveStatistics = new org.apache.commons.math3.stat.descriptive.DescriptiveStatistics(closingPricesArr);

        Double standardDeviation = descriptiveStatistics.getStandardDeviation();
        if (standardDeviation.isNaN()) {
            return Optional.empty();
        }

        Double mean = descriptiveStatistics.getMean();
        Double dailyVolatility = calculateVolatility(standardDeviation, mean);
        Double weeklyVolatility = dailyVolatility * Math.sqrt(7);
        Double monthlyVolatility = dailyVolatility * Math.sqrt(30);
        Double yearlyVolatility = (periodLength >= 365) ? dailyVolatility * Math.sqrt(365) : null;

        return Optional.of(new DescriptiveStatisticsDto(
                mean, new Median().evaluate(closingPricesArr), standardDeviation, descriptiveStatistics.getKurtosis(),
                descriptiveStatistics.getSkewness(), descriptiveStatistics.getMin(), descriptiveStatistics.getMax(),
                descriptiveStatistics.getN(), dailyVolatility, weeklyVolatility, monthlyVolatility, yearlyVolatility
        ));
    }

    public static Optional<StatisticalRelationshipDto> calculateStatisticalRelationship(List<Double> xClosingPrices, List<Double> yClosingPrices,
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

    public static Double calculateVolatility(Double standardDeviation, Double mean) {
        return (standardDeviation / mean) * 100;
    }

    private static List<Double> trimBasedOnPeriod(List<Double> prices, Integer periodLength) {
        if (periodLength != Integer.MAX_VALUE) {
            return prices.subList(Math.max(0, prices.size() - periodLength), prices.size());
        }
        return prices;
    }
}
