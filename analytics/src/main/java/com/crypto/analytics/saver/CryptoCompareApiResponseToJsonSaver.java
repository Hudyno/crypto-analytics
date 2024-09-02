package com.crypto.analytics.saver;

import com.crypto.analytics.api.AnalyticsCryptoCompareDataApi;
import com.crypto.analytics.persistence.dto.PairLight;
import com.crypto.analytics.persistence.service.AnalyticsPairService;
import com.crypto.analytics.util.CollectionUtil;
import com.crypto.analytics.util.JsonUtil;
import com.crypto.cryptocompare.api.configuration.CryptoCompareConfig;
import com.crypto.cryptocompare.api.data.request.HistoricalOHLCVParameters;
import com.crypto.persistence.model.ExchangeType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoCompareApiResponseToJsonSaver extends ApiResponseToJsonSaver {

    private final AnalyticsPairService analyticsPairService;
    private final AnalyticsCryptoCompareDataApi analyticsCryptoCompareDataApi;

    public CryptoCompareApiResponseToJsonSaver(JsonUtil jsonUtil, CollectionUtil collectionUtil,
                                               CryptoCompareConfig cryptoCompareConfig,
                                               AnalyticsPairService analyticsPairService, AnalyticsCryptoCompareDataApi analyticsCryptoCompareDataApi) {

        super(jsonUtil, collectionUtil, cryptoCompareConfig.getRateLimit(), cryptoCompareConfig.getRefreshRate());
        this.analyticsPairService = analyticsPairService;
        this.analyticsCryptoCompareDataApi = analyticsCryptoCompareDataApi;
    }

    public void saveCexHistoricalOHLCV(String fileName) {
        List<PairLight> pairs = analyticsPairService.getDistinctPairsByPreferredQuoteSymbols(ExchangeType.CEX);
        List<HistoricalOHLCVParameters> parameters = createInceptionOHLCVRequest(pairs);

        this.batchSaveApiResponse(
                (subList, params) -> analyticsCryptoCompareDataApi.getCexDailyHistoricalOHLCV((List<HistoricalOHLCVParameters>) subList),
                fileName,
                parameters
        );
    }

    public void saveDexHistoricalOHLCV(String fileName) {
        List<PairLight> pairs = analyticsPairService.getDistinctPairsByPreferredQuoteSymbols(ExchangeType.DEX);
        List<HistoricalOHLCVParameters> parameters = createInceptionOHLCVRequest(pairs);

        this.batchSaveApiResponse(
                (subList, params) -> analyticsCryptoCompareDataApi.getDexDailyHistoricalOHLCV((List<HistoricalOHLCVParameters>) subList),
                fileName,
                parameters
        );
    }

    private List<HistoricalOHLCVParameters> createInceptionOHLCVRequest(List<PairLight> pairs) {
        return pairs.stream()
                    .map(pair -> {
                        HistoricalOHLCVParameters request = new HistoricalOHLCVParameters(pair.getExchange(), pair.getBaseSymbol() + '-' + pair.getQuoteSymbol());
                        request.setLimit(5000);
                        return request;
                    }).toList();
    }

}
