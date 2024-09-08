package com.crypto.analytics.composer.entity;

import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.Metrics;
import com.crypto.persistence.service.CoinService;
import com.crypto.persistence.service.MetricsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MetricsComposer {

    private final CoinService coinService;
    private final MetricsService service;

    public Optional<Metrics> fromValues(String coinId) {
        Optional<Coin> coin = coinService.findById(coinId);
        if (coin.isEmpty()) {
            return Optional.empty();
        }

        Metrics metrics = new Metrics();
        metrics.setCoin(coin.get());
        return Optional.of(metrics);
    }

    @Transactional
    public Optional<Metrics> saveFromValues(String coinId) {
        Optional<Metrics> metrics = fromValues(coinId);
        metrics.ifPresent(service::save);
        return metrics;
    }

}
