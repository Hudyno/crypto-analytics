package com.crypto.persistence.repository;

import com.crypto.persistence.model.ExchangeType;
import com.crypto.persistence.model.Pair;
import com.crypto.persistence.projection.PairExchangeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PairRepository extends JpaRepository<Pair, Long> {

    List<PairExchangeProjection> findByExchange_ExchangeType(ExchangeType exchangeType);

}
