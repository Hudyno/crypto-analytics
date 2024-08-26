package com.crypto.analytics.saver;

import com.crypto.analytics.api.AnalyticsCryptoCompareMinApi;
import com.crypto.analytics.util.CollectionUtil;
import com.crypto.analytics.util.JsonUtil;
import com.crypto.cryptocompare.api.configuration.CryptoCompareConfig;
import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.cryptocompare.api.data.response.Response;
import com.crypto.cryptocompare.api.min.request.HistoricalOHLCVRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CryptoCompareApiResponseToJsonSaver extends ApiResponseToJsonSaver {

    private final AnalyticsCryptoCompareMinApi analyticsCryptoCompareMinApi;

    public CryptoCompareApiResponseToJsonSaver(JsonUtil jsonUtil, CollectionUtil collectionUtil,
                                               AnalyticsCryptoCompareMinApi analyticsCryptoCompareMinApi, CryptoCompareConfig cryptoCompareConfig) {

        super(jsonUtil, collectionUtil, cryptoCompareConfig.getRateLimit(), cryptoCompareConfig.getRefreshRate());
        this.analyticsCryptoCompareMinApi = analyticsCryptoCompareMinApi;
    }

    public void saveCexHistoricalOHLCV(String fileName) {
        List<PairLight> pairs = analyticsPairService.getDistinctPairsByPreferredQuoteSymbols(ExchangeType.CEX);
        List<com.crypto.cryptocompare.api.data.request.HistoricalOHLCVRequest> requestList = pairs.stream()
                .map(pair -> {
                    com.crypto.cryptocompare.api.data.request.HistoricalOHLCVRequest request = new com.crypto.cryptocompare.api.data.request.HistoricalOHLCVRequest(pair.getExchange(), pair.getBaseSymbol() + '-' + pair.getQuoteSymbol());
                    request.setLimit(5000);
                    return request;
                }).toList();

        this.batchSaveApiResponse(
                (subList, params) -> analyticsCryptoCompareDataApi.getCexDailyHistoricalOHLCV((List<com.crypto.cryptocompare.api.data.request.HistoricalOHLCVRequest>) subList),
                fileName,
                requestList
        );
    }

        List<HistoricalOHLCVRequest> requestList = assetSummaryList.stream()
                                                                  .map(coin -> {
                                                                      HistoricalOHLCVRequest request = new HistoricalOHLCVRequest(coin.getSymbol().toUpperCase(), quoteSymbol);
                                                                      request.setAllData(true);
                                                                      return request;
                                                                  })
                                                                  .toList();

        this.batchSaveApiResponse(
                (subList, params) -> analyticsCryptoCompareMinApi.getSymbolHistoricalOHLCV((List<HistoricalOHLCVRequest>) subList),
                fileName,
                requestList
        );
    }

}
