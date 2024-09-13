package com.crypto.persistence.service;

import com.crypto.persistence.dto.PairExchangeDetailDto;
import com.crypto.persistence.dto.PairExchangesDto;
import com.crypto.persistence.mapper.dto.PairExchangeDetailDtoMapper;
import com.crypto.persistence.model.ExchangeType;
import com.crypto.persistence.model.Pair;
import com.crypto.persistence.projection.PairExchangeProjection;
import com.crypto.persistence.repository.PairRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PairService extends BaseService<Pair, Long> {

    private final PairRepository pairRepository;
    private final PairExchangeDetailDtoMapper pairExchangeDetailDtoMapper;

    public PairService(JpaRepository<Pair, Long> repository, PairRepository pairRepository,
                       PairExchangeDetailDtoMapper pairExchangeDetailDtoMapper) {
        super(repository);
        this.pairRepository = pairRepository;
        this.pairExchangeDetailDtoMapper = pairExchangeDetailDtoMapper;
    }

    public List<PairExchangesDto> getAllPairsByExchangeType(ExchangeType type) {
        List<PairExchangeProjection> projections = pairRepository.findByExchange_ExchangeType(type);

        Map<String, Map<String, List<PairExchangeProjection>>> groupedByBaseAndQuoteSymbol = projections.stream()
                                                                                                        .collect(Collectors.groupingBy(PairExchangeProjection::getBaseSymbol,
                                                                                                                 Collectors.groupingBy(PairExchangeProjection::getQuoteSymbol)));

        return groupedByBaseAndQuoteSymbol.values().stream()
                .flatMap(quoteSymbolGroups -> quoteSymbolGroups.values().stream()
                                                                        .map(projectionsGroup -> {
                                                                            List<PairExchangeDetailDto> details = pairExchangeDetailDtoMapper.fromPairExchangeProjectionList(projectionsGroup);
                                                                            return new PairExchangesDto(projectionsGroup.get(0).getBaseSymbol(), projectionsGroup.get(0).getQuoteSymbol(), details);
                                                                        }))
                .toList();
    }
}
