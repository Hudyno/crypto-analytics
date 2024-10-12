package com.crypto.analytics.populate;

import com.crypto.analytics.composer.entity.CoinComposer;
import com.crypto.analytics.composer.entity.HistoricalSupplyEntryComposer;
import com.crypto.analytics.composer.entity.PriceTimeSeriesEntryComposer;
import com.crypto.analytics.util.JsonUtil;
import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.cryptocompare.api.data.response.CexHistoricalOHLCV;
import com.crypto.cryptocompare.api.data.response.DexHistoricalOHLCV;
import com.crypto.cryptocompare.api.data.response.HistoricalOHLCV;
import com.crypto.cryptocompare.api.data.response.HistoricalSupply;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.HistoricalSupplyEntry;
import com.crypto.persistence.model.PriceTimeSeriesEntry;
import com.crypto.persistence.service.CoinService;
import com.crypto.persistence.service.HistoricalSupplyEntryService;
import com.crypto.persistence.service.PriceTimeSeriesEntryService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Log4j2
public class JsonToDatabasePopulate {

    private final PriceTimeSeriesEntryComposer priceTimeSeriesEntryComposer;
    private final CoinComposer coinComposer;
    private final CoinService coinService;
    private final JsonUtil jsonUtil;
    private final PriceTimeSeriesEntryService priceTimeSeriesEntryService;
    private final HistoricalSupplyEntryComposer historicalSupplyEntryComposer;
    private final HistoricalSupplyEntryService historicalSupplyEntryService;

    public void populateCoins(String fileName) {
        List<AssetSummary> response = jsonUtil.readJson(new File(fileName), new TypeReference<>(){});

        List<Coin> coinList = coinComposer.fromAssetSummary(response);
        coinService.saveAll(coinList);
    }

    public void populateCexDailyHistoricalOHLCV(String fileName) {
        List<CexHistoricalOHLCV> response = jsonUtil.readJson(new File(fileName), new TypeReference<>(){});

        List<CexHistoricalOHLCV> cleanedList = removeExisting(response);
        List<PriceTimeSeriesEntry> entries = priceTimeSeriesEntryComposer.fromHistoricalOHLCV(cleanedList);
        priceTimeSeriesEntryService.saveAll(entries);
    }

    public void populateDexDailyHistoricalOHLCV(String fileName) {
        List<DexHistoricalOHLCV> response = jsonUtil.readJson(new File(fileName), new TypeReference<>(){});

        List<DexHistoricalOHLCV> cleanedList = removeExisting(response);
        List<PriceTimeSeriesEntry> entries = priceTimeSeriesEntryComposer.fromHistoricalOHLCV(cleanedList);
        priceTimeSeriesEntryService.saveAll(entries);
    }

    public void populateHistoricalSupply(String fileName) {
        List<HistoricalSupply> response = jsonUtil.readJson(new File(fileName), new TypeReference<>(){});
        List<HistoricalSupplyEntry> entries = historicalSupplyEntryComposer.fromHistoricalSupply(response);
        historicalSupplyEntryService.saveAll(entries);
    }

    private <T extends HistoricalOHLCV> List<T>  removeExisting(List<T> historicalOHLCVS) {
        Set<String> baseSymbols = priceTimeSeriesEntryService.findAllBaseSymbols();

        return historicalOHLCVS.stream()
                               .filter(it -> !baseSymbols.contains(it.getBase()))
                               .toList();
    }
}
