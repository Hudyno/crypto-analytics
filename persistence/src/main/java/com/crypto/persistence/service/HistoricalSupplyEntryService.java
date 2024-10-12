package com.crypto.persistence.service;

import com.crypto.persistence.dto.CoinCirculatingSuppliesDto;
import com.crypto.persistence.dto.TimeCirculatingSupplyDto;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.HistoricalSupplyEntry;
import com.crypto.persistence.projection.CoinCirculatingSupplyProjection;
import com.crypto.persistence.repository.HistoricalSupplyEntryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class HistoricalSupplyEntryService extends BaseService<HistoricalSupplyEntry, Long> {

    private final HistoricalSupplyEntryRepository historicalSupplyEntryRepository;

    public HistoricalSupplyEntryService(JpaRepository<HistoricalSupplyEntry, Long> repository,
                                        HistoricalSupplyEntryRepository historicalSupplyEntryRepository) {
        super(repository);
        this.historicalSupplyEntryRepository = historicalSupplyEntryRepository;
    }

    public Optional<CoinCirculatingSuppliesDto> getCoinCirculatingSuppliesByDate(Coin coin, ZonedDateTime timeStart, ZonedDateTime timeEnd) {
        List<CoinCirculatingSupplyProjection> coinsCirculatingSupplies = historicalSupplyEntryRepository.findAllByBaseSymbolAndTimeBetween(coin, timeStart, timeEnd);

        if (CollectionUtils.isEmpty(coinsCirculatingSupplies)) {
            return Optional.empty();
        }

        List<TimeCirculatingSupplyDto> timeCirculatingSupply = coinsCirculatingSupplies.stream()
                                                                                       .map(it -> new TimeCirculatingSupplyDto(it.getTime(), it.getSupplyCirculating()))
                                                                                       .toList();
        return Optional.of(
                new CoinCirculatingSuppliesDto(coinsCirculatingSupplies.get(0).getBaseSymbolSymbol(), timeCirculatingSupply)
        );
    }
}
