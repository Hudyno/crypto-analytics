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

    public static StatisticalRelationshipDto calculateStatisticalRelationship(List<Double> xClosingPrices, List<Double> yClosingPrices,
                                                                              Integer periodLength) {

        xClosingPrices = xClosingPrices.subList(Math.max(0, xClosingPrices.size() - periodLength), xClosingPrices.size());
        yClosingPrices = yClosingPrices.subList(Math.max(0, yClosingPrices.size() - periodLength), yClosingPrices.size());

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
}
