package com.crypto.analytics.persistence.repository;

import com.crypto.analytics.dto.PairLightDto;
import com.crypto.persistence.model.ExchangeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnalyticsPairRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PairLightDto> findDistinctPairsByFirstExchange(ExchangeType exchangeType) {
        String query =
                "SELECT NEW com.crypto.analytics.persistence.dto.PairLightDto(" +
                        "p.baseSymbol, " +
                        "p.quoteSymbol, " +
                        "MIN(e.name) AS exchange" +
                ") " +
                "FROM Pair p " +
                "JOIN p.exchange e " +
                "WHERE e.exchangeType = :exchangeType " +
                "GROUP BY p.baseSymbol, p.quoteSymbol";

        return entityManager.createQuery(query, PairLightDto.class)
                            .setParameter("exchangeType", exchangeType)
                            .getResultList();
    }
}
