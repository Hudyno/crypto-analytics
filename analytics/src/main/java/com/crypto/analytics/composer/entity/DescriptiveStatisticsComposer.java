package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.DescriptiveStatisticsMapper;
import com.crypto.analytics.model.DescriptiveStatisticsDto;
import com.crypto.persistence.model.DescriptiveStatistics;
import com.crypto.persistence.model.IntervalType;
import com.crypto.persistence.model.Metrics;
import com.crypto.persistence.service.DescriptiveStatisticsService;
import com.crypto.persistence.service.MetricsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DescriptiveStatisticsComposer {

    private final MetricsService metricsService;
    private final DescriptiveStatisticsService service;
    private final DescriptiveStatisticsMapper mapper;

    public Optional<DescriptiveStatistics> fromDescriptiveStatisticsDto(String metricsId, DescriptiveStatisticsDto dto, IntervalType interval) {
        Optional<Metrics> metrics = metricsService.findById(metricsId);
        if (metrics.isEmpty()) {
            return Optional.empty();
        }

        DescriptiveStatistics statistics = mapper.fromDescriptiveStatisticsDto(dto);
        statistics.setMetrics(metrics.get());
        statistics.setInterval(interval);
        return Optional.of(statistics);
    }

    @Transactional
    public Optional<DescriptiveStatistics> saveFromDescriptiveStatisticsDto(String metricsId, DescriptiveStatisticsDto dto,
                                                                            IntervalType interval) {
        Optional<DescriptiveStatistics> statistics = fromDescriptiveStatisticsDto(metricsId, dto, interval);
        statistics.ifPresent(service::save);
        return statistics;
    }
}
