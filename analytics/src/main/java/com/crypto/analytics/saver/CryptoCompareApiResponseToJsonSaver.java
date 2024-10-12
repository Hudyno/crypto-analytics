package com.crypto.analytics.saver;

import com.crypto.analytics.api.AnalyticsCryptoCompareDataApi;
import com.crypto.analytics.dto.PairLightDto;
import com.crypto.analytics.service.AnalyticsPairService;
import com.crypto.analytics.util.CollectionUtil;
import com.crypto.analytics.util.JsonUtil;
import com.crypto.cryptocompare.api.configuration.CryptoCompareConfig;
import com.crypto.cryptocompare.api.data.request.HistoricalOHLCVParameters;
import com.crypto.cryptocompare.api.data.request.HistoricalSupplyParameters;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.model.ExchangeType;
import com.crypto.persistence.service.CoinService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoCompareApiResponseToJsonSaver extends ApiResponseToJsonSaver {

    private final AnalyticsPairService analyticsPairService;
    private final AnalyticsCryptoCompareDataApi analyticsCryptoCompareDataApi;
    private final CoinService coinService;

    public CryptoCompareApiResponseToJsonSaver(JsonUtil jsonUtil, CollectionUtil collectionUtil,
                                               CryptoCompareConfig cryptoCompareConfig, CoinService coinService,
                                               AnalyticsPairService analyticsPairService, AnalyticsCryptoCompareDataApi analyticsCryptoCompareDataApi) {

        super(jsonUtil, collectionUtil, cryptoCompareConfig.getRateLimit(), cryptoCompareConfig.getRefreshRate());
        this.analyticsPairService = analyticsPairService;
        this.analyticsCryptoCompareDataApi = analyticsCryptoCompareDataApi;
        this.coinService = coinService;
    }

    public void saveCexHistoricalOHLCV(String fileName) {
        List<PairLightDto> pairs = analyticsPairService.getDistinctPairsByPreferredQuoteSymbols(ExchangeType.CEX);
        List<HistoricalOHLCVParameters> parameters = createInceptionOHLCVRequest(pairs);

        this.batchSaveApiResponse(
                (subList, params) -> analyticsCryptoCompareDataApi.getCexDailyHistoricalOHLCV((List<HistoricalOHLCVParameters>) subList),
                fileName,
                parameters
        );
    }

    public void saveDexHistoricalOHLCV(String fileName) {
        List<PairLightDto> pairs = analyticsPairService.getDistinctPairsByPreferredQuoteSymbols(ExchangeType.DEX);
        List<HistoricalOHLCVParameters> parameters = createInceptionOHLCVRequest(pairs);

        this.batchSaveApiResponse(
                (subList, params) -> analyticsCryptoCompareDataApi.getDexDailyHistoricalOHLCV((List<HistoricalOHLCVParameters>) subList),
                fileName,
                parameters
        );
    }

    public void saveAllHistoricalSupply(String fileName) {
        List<Coin> coins = coinService.findAll();
        List<HistoricalSupplyParameters> parameters = createHistoricalSupplyRequest(coins);

        this.batchSaveApiResponse(
                (subList, params) -> analyticsCryptoCompareDataApi.getAllHistoricalSupply((List<HistoricalSupplyParameters>) subList),
                fileName,
                parameters
        );
    }

    private List<HistoricalOHLCVParameters> createInceptionOHLCVRequest(List<PairLightDto> pairs) {
        return pairs.stream()
                    .map(pair -> {
                        HistoricalOHLCVParameters request = new HistoricalOHLCVParameters(pair.getExchange(), pair.getBaseSymbol() + '-' + pair.getQuoteSymbol());
                        request.setLimit(5000);
                        return request;
                    }).toList();
    }

    private List<HistoricalSupplyParameters> createHistoricalSupplyRequest(List<Coin> coins) {
        return coins.stream()
                    .map(coin -> {
                        HistoricalSupplyParameters parameters = new HistoricalSupplyParameters(coin.getSymbol());
                        parameters.setLimit(2000);
                        return parameters;
                    }).toList();
    }

}
