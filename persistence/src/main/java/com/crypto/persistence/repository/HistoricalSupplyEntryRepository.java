package com.crypto.persistence.repository;

import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.HistoricalSupplyEntry;
import com.crypto.persistence.projection.CoinCirculatingSupplyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface HistoricalSupplyEntryRepository extends JpaRepository<HistoricalSupplyEntry, Long> {

    List<CoinCirculatingSupplyProjection> findAllByBaseSymbolAndTimeBetween(Coin baseSymbol, ZonedDateTime timeStart, ZonedDateTime timeEnd);

}
