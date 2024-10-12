package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.CoinMapper;
import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.cryptocompare.api.data.response.AssetTopListData;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.service.CoinService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinComposer {

    private final CoinMapper mapper;
    private final CoinService service;

    public Coin fromAssetSummary(AssetSummary summary) {
        Coin coin = mapper.fromAssetSummary(summary);
        coin.setSymbol(summary.getSymbol());
        return coin;
    }

    public List<Coin> fromAssetSummary(List<AssetSummary> assetsSummary) {
        return assetsSummary.stream()
                            .map(this::fromAssetSummary)
                            .toList();
    }

    public Optional<Coin> fromAssetTopListData(AssetTopListData data) {
        Optional<Coin> existingCoin = service.findById(data.getSymbol());

        if (existingCoin.isEmpty()) {
            return Optional.empty();
        }
        mapper.updateFromAssetTopListData(existingCoin.get(), data);
        return existingCoin;
    }

    @Transactional
    public Optional<Coin> updateFromAssetTopListData(AssetTopListData data) {
        Optional<Coin> coin = fromAssetTopListData(data);
        coin.ifPresent(service::save);
        return coin;
    }
}
