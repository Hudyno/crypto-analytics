package com.crypto.persistence.repository;

import com.crypto.persistence.projection.CoinClosingPriceProjection;
import com.crypto.persistence.model.PriceTimeSeriesEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PriceTimeSeriesEntryRepository extends JpaRepository<PriceTimeSeriesEntry, Long> {

    @Query("SELECT DISTINCT c.symbol FROM PriceTimeSeriesEntry p JOIN p.baseSymbol c")
    Set<String> findAllBaseSymbols();

    List<CoinClosingPriceProjection> findBy();
}
