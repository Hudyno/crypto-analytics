package com.crypto.analytics.service;

import com.crypto.persistence.dto.CoinCirculatingSuppliesDto;
import com.crypto.persistence.dto.CoinClosingPricesDto;
import com.crypto.persistence.dto.TimeCirculatingSupplyDto;
import com.crypto.persistence.dto.TimeClosingPriceDto;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.MarketCapTier;
import com.crypto.persistence.service.CoinService;
import com.crypto.persistence.service.HistoricalSupplyEntryService;
import com.crypto.persistence.service.PriceTimeSeriesEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Log4j2
public class IndexService {

    private final PriceTimeSeriesEntryService priceTimeSeriesEntryService;
    private final HistoricalSupplyEntryService historicalSupplyEntryService;
    private final CoinService coinService;

    public void createIndex() {
        List<CoinClosingPricesDto> coinsClosingPrices = priceTimeSeriesEntryService.getAllCoinClosingPricesByTier(MarketCapTier.SMALL_CAP);

        AtomicInteger counter = new AtomicInteger();
        coinsClosingPrices.forEach(it -> {
            List<TimeClosingPriceDto> mutableTimeClosingPrices = new ArrayList<>(it.getTimeClosingPrice());
            mutableTimeClosingPrices.sort(Comparator.comparing(TimeClosingPriceDto::getTime));
            Coin coin = coinService.getReferenceById(it.getSymbol());

            ZonedDateTime timeStart = mutableTimeClosingPrices.get(0).getTime();
            ZonedDateTime timeEnd = mutableTimeClosingPrices.get(mutableTimeClosingPrices.size() - 1).getTime();
            Optional<CoinCirculatingSuppliesDto> circulatingSupplies = historicalSupplyEntryService.getCoinCirculatingSuppliesByDate(coin, timeStart, timeEnd);

            if (circulatingSupplies.isPresent()) {
                List<TimeCirculatingSupplyDto> dto = circulatingSupplies.get().getTimeCirculatingSupply().stream().filter(a -> isNull(a.getSupplyCirculating())).toList();
                if (!CollectionUtils.isEmpty(dto)) {
                    counter.getAndIncrement();
                    log.info("Contains null: {}", counter);
                }
            }
        });
    }
}
