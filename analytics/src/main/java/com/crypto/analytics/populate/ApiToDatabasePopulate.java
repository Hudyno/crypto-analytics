package com.crypto.analytics.populate;

import com.crypto.analytics.api.AnalyticsCryptoCompareDataApi;
import com.crypto.analytics.composer.entity.ExchangeComposer;
import com.crypto.analytics.composer.entity.PairComposer;
import com.crypto.cryptocompare.api.data.request.MarketsAndInstrumentsParameters;
import com.crypto.cryptocompare.api.data.request.MarketsParameters;
import com.crypto.cryptocompare.api.data.response.CexData;
import com.crypto.cryptocompare.api.data.response.DexData;
import com.crypto.cryptocompare.api.data.response.ExchangeData;
import com.crypto.persistence.model.Exchange;
import com.crypto.persistence.model.ExchangeType;
import com.crypto.persistence.model.Pair;
import com.crypto.persistence.service.ExchangeService;
import com.crypto.persistence.service.PairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiToDatabasePopulate {

    private final AnalyticsCryptoCompareDataApi cryptoCompareDataApi;
    private final PairComposer pairComposer;
    private final ExchangeComposer exchangeComposer;
    private final ExchangeService exchangeService;
    private final PairService pairService;

    public void populateCexExchanges() {
        Optional<Map<String, CexData>> response = cryptoCompareDataApi.executeGet(
                params -> cryptoCompareDataApi.getSpotMarkets((MarketsParameters) params), new MarketsParameters()
        );

        Optional<List<Exchange>> exchanges = response.map(
                it -> exchangeComposer.fromExchangeDataMap(it, ExchangeType.CEX)
        );
        exchanges.ifPresent(exchangeService::saveAll);
    }

    public void populateDexExchanges() {
        Optional<Map<String, DexData>> response = cryptoCompareDataApi.executeGet(
                params -> cryptoCompareDataApi.getDexMarkets((MarketsParameters) params), new MarketsParameters()
        );

        Optional<List<Exchange>> exchanges = response.map(
                it -> exchangeComposer.fromExchangeDataMap(it, ExchangeType.DEX)
        );
        exchanges.ifPresent(exchangeService::saveAll);
    }

    public void populateCexAssets() {
        Optional<Map<String, CexData>> response = cryptoCompareDataApi.executeGet(
                params -> cryptoCompareDataApi.getSpotMarketsAndInstruments((MarketsAndInstrumentsParameters) params), new MarketsAndInstrumentsParameters()
        );

        response.ifPresent(this::processExchangeData);
    }

    public void populateDexAssets() {
        Optional<Map<String, DexData>> response = cryptoCompareDataApi.executeGet(
                params -> cryptoCompareDataApi.getDexMarketsAndInstruments((MarketsAndInstrumentsParameters) params), new MarketsAndInstrumentsParameters()
        );

        response.ifPresent(this::processExchangeData);
    }

    private void processExchangeData(Map<String,? extends ExchangeData> exchangeNameExchangeData) {
        List<? extends ExchangeData> data = exchangeNameExchangeData.values().stream().toList();

        List<Pair> pairs = data.stream()
                               .flatMap(it -> it.getInstrumentDataMap().entrySet().stream())
                               .flatMap(entry -> pairComposer.fromInstrumentData(entry.getValue(), entry.getKey()).stream())
                               .toList();

        pairService.saveAll(pairs);
    }
}
