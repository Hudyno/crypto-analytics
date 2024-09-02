package com.crypto.analytics.api;

import com.crypto.cryptocompare.api.data.CryptoCompareDataApi;
import com.crypto.cryptocompare.api.data.CryptoCompareDataClient;
import com.crypto.cryptocompare.api.data.request.HistoricalOHLCVParameters;
import com.crypto.cryptocompare.api.data.request.RequestParameters;
import com.crypto.cryptocompare.api.data.response.CexHistoricalOHLCV;
import com.crypto.cryptocompare.api.data.response.DexHistoricalOHLCV;
import com.crypto.cryptocompare.api.data.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class AnalyticsCryptoCompareDataApi extends CryptoCompareDataApi {

    public AnalyticsCryptoCompareDataApi(CryptoCompareDataClient cryptoCompareDataClient) {
        super(cryptoCompareDataClient);
    }

    public <T> Optional<T> executeGet(Function<RequestParameters, Response<T>> methodCall, RequestParameters parameters) {
        Response<T> response = methodCall.apply(parameters);

        if (nonNull(response.getErr()) && nonNull(response.getErr().getMessage())) {
            log.error(response.getErr().getMessage());
            return Optional.empty();
        }
        return Optional.of(response.getData());
    }

    public List<CexHistoricalOHLCV> getCexDailyHistoricalOHLCV(List<HistoricalOHLCVParameters> parameters) {
        return parameters.stream()
                         .map(param -> executeGet(
                                 paramProxy -> this.getCexDailyHistoricalOHLCV((HistoricalOHLCVParameters) paramProxy), param
                         ))
                         .flatMap(Optional::stream)
                         .flatMap(Collection::stream)
                         .toList();
    }

    public List<DexHistoricalOHLCV> getDexDailyHistoricalOHLCV(List<HistoricalOHLCVParameters> parameters) {
        return parameters.stream()
                         .map(param -> executeGet(
                                 paramProxy -> this.getDexDailyHistoricalOHLCV((HistoricalOHLCVParameters) paramProxy), param
                         ))
                         .flatMap(Optional::stream)
                         .flatMap(Collection::stream)
                         .toList();
    }

}
