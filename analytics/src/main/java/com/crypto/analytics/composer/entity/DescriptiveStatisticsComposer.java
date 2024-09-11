package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.DescriptiveStatisticsMapper;
import com.crypto.analytics.dto.DescriptiveStatisticsDto;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.DescriptiveStatistics;
import com.crypto.persistence.model.IntervalType;
import com.crypto.persistence.model.Period;
import com.crypto.persistence.service.CoinService;
import com.crypto.persistence.service.DescriptiveStatisticsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DescriptiveStatisticsComposer {

    private final CoinService coinService;
    private final DescriptiveStatisticsService service;
    private final DescriptiveStatisticsMapper mapper;

    public Optional<DescriptiveStatistics> fromDescriptiveStatisticsDto(String symbol, DescriptiveStatisticsDto dto,
                                                                        IntervalType interval, Period period) {
        Optional<Coin> coin = coinService.findById(symbol);
        if (coin.isEmpty()) {
            return Optional.empty();
        }

        DescriptiveStatistics statistics = mapper.fromDescriptiveStatisticsDto(dto);
        statistics.setSymbol(coin.get());
        statistics.setInterval(interval);
        statistics.setPeriod(period);
        return Optional.of(statistics);
    }

    @Transactional
    public Optional<DescriptiveStatistics> saveFromDescriptiveStatisticsDto(String symbol, DescriptiveStatisticsDto dto,
                                                                            IntervalType interval, Period period) {
        Optional<DescriptiveStatistics> statistics = fromDescriptiveStatisticsDto(symbol, dto, interval, period);
        statistics.ifPresent(service::save);
        return statistics;
    }
}
