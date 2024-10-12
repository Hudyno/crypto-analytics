package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.HistoricalSupplyEntryMapper;
import com.crypto.cryptocompare.api.data.response.HistoricalSupply;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.HistoricalSupplyEntry;
import com.crypto.persistence.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoricalSupplyEntryComposer {

    private final HistoricalSupplyEntryMapper mapper;
    private final CoinService coinService;

    public HistoricalSupplyEntry fromHistoricalSupply(Coin coin, HistoricalSupply supply) {
        HistoricalSupplyEntry entry = mapper.fromHistoricalSupply(supply);
        entry.setBaseSymbol(coin);

        return entry;
    }

    public List<HistoricalSupplyEntry> fromHistoricalSupply(List<HistoricalSupply> supplyList) {
        List<Coin> coins = coinService.findAll();

        Map<String, Coin> idCoins = coins.stream().collect(Collectors.toMap(
                Coin::getSymbol,
                it -> it
        ));

        return supplyList.stream()
                         .filter(it -> idCoins.containsKey(it.getSymbol()))
                         .map(it -> fromHistoricalSupply(idCoins.get(it.getSymbol()), it))
                         .toList();
    }
}
