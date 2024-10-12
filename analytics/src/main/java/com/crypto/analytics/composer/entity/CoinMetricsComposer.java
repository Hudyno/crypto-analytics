package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.CoinMetricsMapper;
import com.crypto.cryptocompare.api.data.response.AssetTopListData;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.CoinMetrics;
import com.crypto.persistence.service.CoinMetricsService;
import com.crypto.persistence.service.CoinService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinMetricsComposer {

    private final CoinService coinService;
    private final CoinMetricsService service;
    private final CoinMetricsMapper mapper;

    public Optional<CoinMetrics> fromAssetTopListData(AssetTopListData data) {
        Optional<Coin> coin = coinService.findById(data.getSymbol());
        if (coin.isEmpty()) {
            return Optional.empty();
        }

        CoinMetrics metrics = mapper.fromAssetTopListData(data);
        metrics.setCoin(coin.get());
        return Optional.of(metrics);
    }

    @Transactional
    public Optional<CoinMetrics> saveFromAssetTopListData(AssetTopListData data) {
        Optional<CoinMetrics> metrics = fromAssetTopListData(data);
        metrics.ifPresent(service::save);
        return metrics;
    }
}
