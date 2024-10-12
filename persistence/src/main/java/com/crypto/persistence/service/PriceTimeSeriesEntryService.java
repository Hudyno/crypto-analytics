package com.crypto.persistence.service;

import com.crypto.persistence.dto.CoinClosingPricesDto;
import com.crypto.persistence.dto.TimeClosingPriceDto;
import com.crypto.persistence.model.MarketCapTier;
import com.crypto.persistence.projection.CoinClosingPriceProjection;
import com.crypto.persistence.model.PriceTimeSeriesEntry;
import com.crypto.persistence.repository.PriceTimeSeriesEntryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PriceTimeSeriesEntryService extends BaseService<PriceTimeSeriesEntry, Long>{

    private final PriceTimeSeriesEntryRepository entryRepository;

    public PriceTimeSeriesEntryService(JpaRepository<PriceTimeSeriesEntry, Long> repository, PriceTimeSeriesEntryRepository entryRepository) {
        super(repository);
        this.entryRepository = entryRepository;
    }

    public Set<String> findAllBaseSymbols() {
        return this.entryRepository.findAllBaseSymbols();
    }

    public List<CoinClosingPricesDto> getAllCoinClosingPrices() {
        List<CoinClosingPriceProjection> coinsClosingPrices = entryRepository.findBy();
        return map(coinsClosingPrices);
    }

    public List<CoinClosingPricesDto> getAllCoinClosingPricesByTier(MarketCapTier tier) {
        List<CoinClosingPriceProjection> coinsClosingPrices = entryRepository.findAllByBaseSymbol_Tier(tier);
        return map(coinsClosingPrices);
    }

    private List<CoinClosingPricesDto> map(List<CoinClosingPriceProjection> projections) {
        Map<String, List<CoinClosingPriceProjection>> groupedBySymbol = projections.stream()
                                                                                   .collect(Collectors.groupingBy(CoinClosingPriceProjection::getBaseSymbolSymbol));

        return groupedBySymbol.entrySet().stream()
                .map(entry -> {
                    List<TimeClosingPriceDto> timeClosingPrice = entry.getValue().stream()
                                                                                 .map(it -> new TimeClosingPriceDto(it.getTime(), it.getClose()))
                                                                                 .toList();

                    return new CoinClosingPricesDto(entry.getKey(), timeClosingPrice);
                })
                .toList();
    }


}
