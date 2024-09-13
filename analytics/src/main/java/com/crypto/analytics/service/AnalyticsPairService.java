package com.crypto.analytics.service;

import com.crypto.analytics.dto.PairLightDto;
import com.crypto.persistence.dto.PairExchangeDetailDto;
import com.crypto.persistence.dto.PairExchangesDto;
import com.crypto.persistence.model.ExchangeType;
import com.crypto.persistence.service.PairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsPairService {

    private final PairService pairService;

    private final Map<String, Integer> preferredQuoteSymbols = Map.of(
            "USD", 1, "USDT", 2, "USDC", 3, "DAI", 4, "BTC", 5, "ETH", 6
    );

    public List<PairLightDto> getDistinctPairsByPreferredQuoteSymbols(ExchangeType exchangeType) {
        List<PairExchangesDto> pairsExchanges = pairService.getAllPairsByExchangeType(exchangeType);

        Map<String, List<PairExchangesDto>> associatedQuotesToBaseSymbol = pairsExchanges.stream()
                                                                                         .collect(Collectors.groupingBy(PairExchangesDto::getBaseSymbol));

        List<PairExchangesDto> bestQuotePairs = associatedQuotesToBaseSymbol.values().stream()
                                                                                     .map(this::selectPairWithBestQuote).toList();

        return bestQuotePairs.stream()
                             .map(it -> {
                                 PairExchangeDetailDto detail = selectDetailWithBestExchange(it.getExchangesDetails());
                                 return new PairLightDto(it.getBaseSymbol(), it.getQuoteSymbol(), detail.getExchange());
                             })
                             .toList();
    }

    private PairExchangesDto selectPairWithBestQuote(List<PairExchangesDto> pairsExchanges) {
        return pairsExchanges.stream()
                             .min((p1, p2) -> {
                                 int index1 = preferredQuoteSymbols.getOrDefault(p1.getQuoteSymbol(), Integer.MAX_VALUE);
                                 int index2 = preferredQuoteSymbols.getOrDefault(p2.getQuoteSymbol(), Integer.MAX_VALUE);
                                 return Integer.compare(index1, index2);
                             })
                             .orElse(pairsExchanges.get(0));

    }

    private PairExchangeDetailDto selectDetailWithBestExchange(List<PairExchangeDetailDto> detail) {
        Optional<ZonedDateTime> earliestFirstTradeTime = detail.stream()
                                                               .map(PairExchangeDetailDto::getFirstTradeTime)
                                                               .min(Comparator.naturalOrder());

        Optional<ZonedDateTime> latestLastTradeTime = detail.stream()
                                                            .map(PairExchangeDetailDto::getLastTradeTime)
                                                            .max(Comparator.naturalOrder());

        List<PairExchangeDetailDto> hasEarliestFirstTradeTime = detail.stream()
                                                                      .filter(it -> it.getFirstTradeTime() == earliestFirstTradeTime.get())
                                                                      .toList();

        List<PairExchangeDetailDto> hasLatestLastTradeTime = detail.stream()
                                                                   .filter(it -> it.getLastTradeTime() == latestLastTradeTime.get())
                                                                   .toList();

        List<PairExchangeDetailDto> intermediaryBestMatch = hasEarliestFirstTradeTime.stream()
                                                                                     .filter(hasLatestLastTradeTime::contains)
                                                                                     .toList();

        if (intermediaryBestMatch.isEmpty()) {
            return hasEarliestFirstTradeTime.stream().max(Comparator.comparing(PairExchangeDetailDto::getTotalTrades)).get();
        }

        return intermediaryBestMatch.stream().max(Comparator.comparing(PairExchangeDetailDto::getTotalTrades)).get();
    }
}
