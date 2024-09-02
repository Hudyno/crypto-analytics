package com.crypto.analytics.persistence.service;

import com.crypto.analytics.persistence.dto.PairLight;
import com.crypto.analytics.persistence.repository.AnalyticsPairRepository;
import com.crypto.persistence.model.ExchangeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsPairService {

    private final AnalyticsPairRepository analyticsPairRepository;

    private final Map<String, Integer> preferredQuoteSymbols = Map.of(
            "USD", 1, "USDT", 2, "USDC", 3, "DAI", 4, "BTC", 5, "ETH", 6
    );

    public List<PairLight> getDistinctPairsByPreferredQuoteSymbols(ExchangeType exchangeType) {
        List<PairLight> pairs = analyticsPairRepository.findDistinctPairsByFirstExchange(exchangeType);

        Map<String, List<PairLight>> associatedQuotesToBaseSymbol = pairs.stream().collect(Collectors.groupingBy(PairLight::getBaseSymbol));

        return associatedQuotesToBaseSymbol.values().stream()
                                                    .map(this::getBestQuote)
                                                    .toList();
    }

    private PairLight getBestQuote(List<PairLight> pairs) {
        return pairs.stream()
                    .min((p1, p2) -> {
                        int index1 = preferredQuoteSymbols.getOrDefault(p1.getQuoteSymbol(), Integer.MAX_VALUE);
                        int index2 = preferredQuoteSymbols.getOrDefault(p2.getQuoteSymbol(), Integer.MAX_VALUE);
                        return Integer.compare(index1, index2);
                    })
                    .orElse(pairs.get(0));
    }
}
