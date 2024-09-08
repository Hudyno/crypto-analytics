package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.PairMapper;
import com.crypto.cryptocompare.api.data.response.ExchangeData;
import com.crypto.cryptocompare.api.data.response.InstrumentData;
import com.crypto.persistence.model.Exchange;
import com.crypto.persistence.model.Pair;
import com.crypto.persistence.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PairComposer {

    private final PairMapper mapper;
    private final ExchangeService exchangeService;

    public Optional<Pair> fromInstrumentData(InstrumentData data, String exchangeName) {
        Optional<Exchange> exchange = exchangeService.getReferenceByIdChecked(exchangeName);

        if (exchange.isEmpty()) {
            return Optional.empty();
        }

        Pair pair = mapper.fromInstrumentData(data);
        pair.setExchange(exchange.get());
        return Optional.of(pair);
    }

    public List<Pair> fromExchangeData(ExchangeData data, String exchangeName) {
        return data.getInstrumentDataMap().values().stream()
                                                   .map(instrument -> this.fromInstrumentData(instrument, exchangeName))
                                                   .flatMap(Optional::stream)
                                                   .toList();
    }
}
