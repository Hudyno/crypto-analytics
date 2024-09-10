package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.StatisticalRelationshipMapper;
import com.crypto.analytics.dto.StatisticalRelationshipDto;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.IntervalType;
import com.crypto.persistence.model.Period;
import com.crypto.persistence.model.StatisticalRelationship;
import com.crypto.persistence.service.CoinService;
import com.crypto.persistence.service.StatisticalRelationshipService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticalRelationshipComposer {

    private final CoinService coinService;
    private final StatisticalRelationshipService service;
    private final StatisticalRelationshipMapper mapper;

    public Optional<StatisticalRelationship> fromStatisticalRelationshipDto(String baseSymbol, String toSymbol,
                                                                            IntervalType interval, Period period,
                                                                            StatisticalRelationshipDto dto) {
        Optional<Coin> baseCoin = coinService.getReferenceByIdChecked(baseSymbol);
        Optional<Coin> toCoin = coinService.getReferenceByIdChecked(toSymbol);

        if (baseCoin.isEmpty() || toCoin.isEmpty()) {
            return Optional.empty();
        }

        StatisticalRelationship relationship = mapper.fromStatisticalRelationshipDto(dto);
        relationship.setBaseSymbol(baseCoin.get());
        relationship.setToSymbol(toCoin.get());
        relationship.setPeriod(period);
        relationship.setInterval(interval);
        return Optional.of(relationship);
    }

    @Transactional
    public Optional<StatisticalRelationship> saveFromStatisticalRelationshipDto(String baseSymbol, String toSymbol,
                                                                                IntervalType interval, Period period,
                                                                                StatisticalRelationshipDto dto) {
        Optional<StatisticalRelationship> relationship = fromStatisticalRelationshipDto(baseSymbol, toSymbol, interval, period, dto);
        relationship.ifPresent(service::save);
        return relationship;
    }
}
