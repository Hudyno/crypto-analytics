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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceTimeSeriesEntryComposer {

    private final PriceTimeSeriesEntryMapper mapper;
    private final ExchangeService exchangeService;
    private final CoinService coinService;

    public Optional<PriceTimeSeriesEntry> fromHistoricalOHLCV(HistoricalOHLCV historicalOHLCV) {
        Optional<Exchange> exchange = exchangeService.getReferenceByIdChecked(historicalOHLCV.getMarket());
        Optional<Coin> baseSymbol = coinService.getReferenceByIdChecked(historicalOHLCV.getBase());

        if (exchange.isEmpty() || baseSymbol.isEmpty()) {
            return Optional.empty();
        }

        PriceTimeSeriesEntry entry = mapper.fromHistoricalOHLCV(historicalOHLCV);
        entry.setExchange(exchange.get());
        entry.setBaseSymbol(baseSymbol.get());

        return Optional.of(entry);
    }

    public List<PriceTimeSeriesEntry> fromHistoricalOHLCV(List<? extends HistoricalOHLCV> historicalOHLCVS) {
        return historicalOHLCVS.stream()
                               .map(this::fromHistoricalOHLCV)
                               .flatMap(Optional::stream)
                               .toList();
    }

}
