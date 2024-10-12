package com.crypto.persistence.service;

import com.crypto.persistence.model.CoinMetrics;
import com.crypto.persistence.repository.CoinMetricsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CoinMetricsService extends BaseService<CoinMetrics, String> {

    private final CoinMetricsRepository coinMetricsRepository;

    public CoinMetricsService(JpaRepository<CoinMetrics, String> repository, CoinMetricsRepository coinMetricsRepository) {
        super(repository);
        this.coinMetricsRepository = coinMetricsRepository;
    }
}
