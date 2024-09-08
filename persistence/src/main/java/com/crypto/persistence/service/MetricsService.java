package com.crypto.persistence.service;

import com.crypto.persistence.model.Metrics;
import com.crypto.persistence.repository.MetricsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MetricsService extends BaseService<Metrics, String> {

    private final MetricsRepository metricsRepository;

    public MetricsService(JpaRepository<Metrics, String> repository, MetricsRepository metricsRepository) {
        super(repository);
        this.metricsRepository = metricsRepository;
    }
}
