package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.CoinMapper;
import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.persistence.model.Coin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinComposer {

    private final CoinMapper mapper;

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
}
