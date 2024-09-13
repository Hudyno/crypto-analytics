package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.PriceTimeSeriesEntryMapper;
import com.crypto.cryptocompare.api.data.response.HistoricalOHLCV;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.Exchange;
import com.crypto.persistence.model.PriceTimeSeriesEntry;
import com.crypto.persistence.service.CoinService;
import com.crypto.persistence.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceTimeSeriesEntryComposer {

    private final PriceTimeSeriesEntryMapper mapper;
    private final ExchangeService exchangeService;
    private final CoinService coinService;

    public PriceTimeSeriesEntry fromHistoricalOHLCV(Coin coin, Exchange exchange, HistoricalOHLCV historicalOHLCV) {
        PriceTimeSeriesEntry entry = mapper.fromHistoricalOHLCV(historicalOHLCV);
        entry.setExchange(exchange);
        entry.setBaseSymbol(coin);

        return entry;
    }

    public List<PriceTimeSeriesEntry> fromHistoricalOHLCV(List<? extends HistoricalOHLCV> historicalOHLCVS) {
        List<Exchange> exchanges = exchangeService.findAll();
        List<Coin> coins = coinService.findAll();

        Map<String, Exchange> idExchanges = exchanges.stream().collect(Collectors.toMap(
                Exchange::getName,
                it -> it
        ));
        Map<String, Coin> idCoins = coins.stream().collect(Collectors.toMap(
                Coin::getSymbol,
                it -> it
        ));

        return historicalOHLCVS.stream()
                               .filter(it -> idCoins.containsKey(it.getBase()) && idExchanges.containsKey(it.getMarket()))
                               .map(it -> fromHistoricalOHLCV(idCoins.get(it.getBase()), idExchanges.get(it.getMarket()), it))
                               .toList();
    }
}
