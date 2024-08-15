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

    public void saveAllHistoricalOHLCV(String fileName, String quoteSymbol) {
        Response<AssetSummary> response = jsonUtil.readJson(new File("all_coins_v2.json"), new TypeReference<>(){});
        List<AssetSummary> assetSummaryList = response.getData().getList();

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
