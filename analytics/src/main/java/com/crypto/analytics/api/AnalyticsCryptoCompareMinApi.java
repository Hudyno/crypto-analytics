package com.crypto.analytics.api;

import com.crypto.analytics.util.CryptoCompareMinUtil;
import com.crypto.cryptocompare.api.min.CryptoCompareMinApi;
import com.crypto.analytics.model.cryptocompare.min.SymbolHistoricalOHLCV;
import com.crypto.cryptocompare.api.min.CryptoCompareMinClient;
import com.crypto.cryptocompare.api.min.request.HistoricalOHLCVRequest;
import com.crypto.cryptocompare.api.min.response.HistoricalOHLCV;
import com.crypto.cryptocompare.api.min.response.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyticsCryptoCompareMinApi extends CryptoCompareMinApi {

    private final CryptoCompareMinUtil cryptoCompareMinUtil;

    public AnalyticsCryptoCompareMinApi(CryptoCompareMinClient cryptoCompareMinClient, CryptoCompareMinUtil cryptoCompareMinUtil) {
        super(cryptoCompareMinClient);
        this.cryptoCompareMinUtil = cryptoCompareMinUtil;
    }

    public Optional<SymbolHistoricalOHLCV> getSymbolHistoricalOHLCV(HistoricalOHLCVRequest request) {
        Optional<Response<HistoricalOHLCV>> response = getDailyPairOHLCV(request);

        return response.map(r -> {
            cryptoCompareMinUtil.removePreTradeEntries(r.getData());
            return new SymbolHistoricalOHLCV(request.getFsym(), request.getTsym(), r.getData());
        });
    }

    public List<SymbolHistoricalOHLCV> getSymbolHistoricalOHLCV(List<HistoricalOHLCVRequest> requestList) {
        return requestList.stream()
                .map(this::getSymbolHistoricalOHLCV)
                .flatMap(Optional::stream)
                .toList();
    }
}
