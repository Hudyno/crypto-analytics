package com.crypto.analytics.populate;

import com.crypto.analytics.composer.entity.DescriptiveStatisticsComposer;
import com.crypto.analytics.composer.entity.MetricsComposer;
import com.crypto.analytics.model.DescriptiveStatisticsDto;
import com.crypto.analytics.util.StatisticsUtil;
import com.crypto.persistence.model.IntervalType;
import com.crypto.persistence.model.Metrics;
import com.crypto.persistence.projection.CoinClosingPriceProjection;
import com.crypto.persistence.service.PriceTimeSeriesEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatabasePopulate {

    private final PriceTimeSeriesEntryService priceTimeSeriesEntryService;
    private final DescriptiveStatisticsComposer descriptiveStatisticsComposer;
    private final MetricsComposer metricsComposer;

    public void populateMetrics() {
        Map<String, List<CoinClosingPriceProjection>> coinsClosingPrices = priceTimeSeriesEntryService.getAllClosingPrices();

        coinsClosingPrices.forEach(this::populateMetrics);
    }

    private void populateMetrics(String symbol, List<CoinClosingPriceProjection> projections) {
        Optional<Metrics> managedMetrics = metricsComposer.saveFromValues(symbol);
        if (managedMetrics.isEmpty()) {
            return;
        }

        List<Double> closingPrices = projections.stream()
                                                .sorted(Comparator.comparing(CoinClosingPriceProjection::getTime))
                                                .map(CoinClosingPriceProjection::getClose)
                                                .toList();

        populateDescriptiveStatistics(managedMetrics.get(), closingPrices);
    }

    private void populateDescriptiveStatistics(Metrics metrics, List<Double> closingPrices) {
        DescriptiveStatisticsDto statisticsDto = StatisticsUtil.calculateDescriptiveStatistics(closingPrices);

        descriptiveStatisticsComposer.saveFromDescriptiveStatisticsDto(metrics.getId(), statisticsDto, IntervalType.DAILY);
    }
}
