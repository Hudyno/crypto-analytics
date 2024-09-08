package com.crypto.persistence.service;

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

    public Map<String, List<CoinClosingPriceProjection>> getAllClosingPrices() {
        List<CoinClosingPriceProjection> coinsClosingPrices = entryRepository.findBy();

        return coinsClosingPrices.stream()
                                 .collect(Collectors.groupingBy(CoinClosingPriceProjection::getBaseSymbolSymbol));

    }


}
