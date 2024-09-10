package com.crypto.analytics.populate;

import com.crypto.analytics.composer.entity.DescriptiveStatisticsComposer;
import com.crypto.analytics.composer.entity.MetricsComposer;
import com.crypto.analytics.composer.entity.StatisticalRelationshipComposer;
import com.crypto.analytics.model.DescriptiveStatisticsDto;
import com.crypto.analytics.model.StatisticalRelationshipDto;
import com.crypto.analytics.service.PriceService;
import com.crypto.analytics.service.StatisticsService;
import com.crypto.persistence.dto.CoinClosingPricesDto;
import com.crypto.persistence.dto.TimeClosingPriceDto;
import com.crypto.persistence.model.IntervalType;
import com.crypto.persistence.model.Metrics;
import com.crypto.persistence.model.Period;
import com.crypto.persistence.service.PriceTimeSeriesEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabasePopulate {

    private final PriceTimeSeriesEntryService priceTimeSeriesEntryService;
    private final DescriptiveStatisticsComposer descriptiveStatisticsComposer;
    private final MetricsComposer metricsComposer;
    private final StatisticalRelationshipComposer statisticalRelationshipComposer;

    public void populateMetrics() {
        List<CoinClosingPricesDto> coinsClosingPrices = priceTimeSeriesEntryService.getAllCoinClosingPrices();

        coinsClosingPrices.forEach(this::populateMetrics);
    }

    private void populateMetrics(CoinClosingPricesDto coinClosingPrices) {
        Optional<Metrics> managedMetrics = metricsComposer.saveFromValues(coinClosingPrices.getSymbol());
        if (managedMetrics.isEmpty()) {
            return;
        }

        populateDescriptiveStatistics(managedMetrics.get(), coinClosingPrices.getClosingPrices());
    }

    private void populateDescriptiveStatistics(Metrics metrics, List<Double> closingPrices) {
        DescriptiveStatisticsDto statisticsDto = StatisticsService.calculateDescriptiveStatistics(closingPrices);

        descriptiveStatisticsComposer.saveFromDescriptiveStatisticsDto(metrics.getId(), statisticsDto, IntervalType.DAILY);
    }

    public void populateStatisticalRelationship(String compareToSymbol) {
        List<CoinClosingPricesDto> allCoinsPrices = new ArrayList<>(priceTimeSeriesEntryService.getAllCoinClosingPrices());
        Optional<CoinClosingPricesDto> yCoinClosingPrices = allCoinsPrices.stream()
                                                                          .filter(it -> it.getSymbol().equals(compareToSymbol))
                                                                          .findFirst();

        if (yCoinClosingPrices.isEmpty()) {
            return;
        }

        allCoinsPrices.remove(yCoinClosingPrices.get());
        List<TimeClosingPriceDto> backup = new ArrayList<>(yCoinClosingPrices.get().getTimeClosingPrice());

        allCoinsPrices.forEach(it -> {
            PriceService.synchronizeClosingPriceIntervals(it.getTimeClosingPrice(), yCoinClosingPrices.get().getTimeClosingPrice());
            populateStatisticalRelationship(it, yCoinClosingPrices.get(), IntervalType.DAILY);

            yCoinClosingPrices.get().setTimeClosingPrice(backup);
        });
    }

    public void populateStatisticalRelationship(CoinClosingPricesDto xCoinClosingPrices, CoinClosingPricesDto yCoinClosingPrices,
                                                 IntervalType interval) {

        Map<Period, Integer> periodToDaysMap = Map.of(
                Period.ALL_TIME, xCoinClosingPrices.getClosingPrices().size(),
                Period.LAST_60D, 60,
                Period.LAST_120D, 120,
                Period.LAST_365D, 365
        );

        periodToDaysMap.forEach((period, nDays) -> {
            if (xCoinClosingPrices.getClosingPrices().size() < nDays || yCoinClosingPrices.getClosingPrices().size() < nDays) {
                return;
            }

            StatisticalRelationshipDto relationship = StatisticsService.calculateStatisticalRelationship(xCoinClosingPrices.getClosingPrices(),
                                                                                                         yCoinClosingPrices.getClosingPrices(),
                                                                                                         nDays);

            statisticalRelationshipComposer.saveFromStatisticalRelationshipDto(
                    xCoinClosingPrices.getSymbol(), yCoinClosingPrices.getSymbol(), interval, period, relationship
            );

        });
    }

}
