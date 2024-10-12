package com.crypto.analytics.api;

import com.crypto.cryptocompare.api.data.CryptoCompareDataApi;
import com.crypto.cryptocompare.api.data.CryptoCompareDataClient;
import com.crypto.cryptocompare.api.data.request.HistoricalOHLCVParameters;
import com.crypto.cryptocompare.api.data.request.HistoricalSupplyParameters;
import com.crypto.cryptocompare.api.data.request.RequestParameters;
import com.crypto.cryptocompare.api.data.request.TopListParameters;
import com.crypto.cryptocompare.api.data.response.AssetTopListData;
import com.crypto.cryptocompare.api.data.response.CexHistoricalOHLCV;
import com.crypto.cryptocompare.api.data.response.DexHistoricalOHLCV;
import com.crypto.cryptocompare.api.data.response.HistoricalSupply;
import com.crypto.cryptocompare.api.data.response.Response;
import com.crypto.cryptocompare.api.data.response.TopListData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class AnalyticsCryptoCompareDataApi extends CryptoCompareDataApi {

    public AnalyticsCryptoCompareDataApi(CryptoCompareDataClient cryptoCompareDataClient) {
        super(cryptoCompareDataClient);
    }

    public <T> Optional<T> executeGet(Function<RequestParameters, Response<T>> methodCall, RequestParameters parameters) {
        Response<T> response;

        try {
            response = methodCall.apply(parameters);

            if (nonNull(response.getErr()) && nonNull(response.getErr().getMessage())) {
                log.error(response.getErr().getMessage());
                return Optional.empty();
            }
        }catch (Exception e) {
            log.error(e.getMessage());
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

    public Optional<TopListData> getTopList(TopListParameters parameters) {
        return executeGet(
                paramProxy -> this.getTopListData((TopListParameters) paramProxy), parameters
        );
    }

    public List<AssetTopListData> getAllTopList(TopListParameters parameters) {
        List<TopListData> topLists = Stream.iterate(1, page -> page + 1)
                                           .flatMap(page -> {
                                               parameters.setPage(page);
                                               return getTopList(parameters).stream();
                                           })
                                           .takeWhile(response -> hasNextPage(response.getStats().getPage(),
                                                                              response.getStats().getPageSize(),
                                                                              response.getStats().getTotalAssets()))
                                           .toList();

        return topLists.stream().flatMap(it -> it.getAssetData().stream()).toList();
    }

    public List<HistoricalSupply> getCompleteHistoricalSupply(HistoricalSupplyParameters parameters) {

        Map<Long, HistoricalSupply> timeHistoricalSupply = new TreeMap<>();

        while(true) {
            Optional<List<HistoricalSupply>> response = executeGet(
                    paramProxy -> this.getHistoricalSupply((HistoricalSupplyParameters) paramProxy), parameters
            );

            if (response.isEmpty()) {
                break;
            }

            HistoricalSupply firstIndexSupply = response.get().remove(0);
            Map<Long, HistoricalSupply> responseMap = response.get().stream()
                                                                    .collect(toMap(HistoricalSupply::getTimestamp, it -> it));

            timeHistoricalSupply.putAll(responseMap);
            parameters.setTo_ts(firstIndexSupply.getTimestamp());

            if (!hasNextPage(firstIndexSupply)) {
                break;
            }
        }
        return timeHistoricalSupply.values().stream().toList();
    }

    public List<HistoricalSupply> getAllHistoricalSupply(List<HistoricalSupplyParameters> parameters) {
        return parameters.stream()
                         .flatMap(it -> getCompleteHistoricalSupply(it).stream())
                         .toList();
    }

    private boolean hasNextPage(Integer page, Integer pageSize, Long totalAssets) {
        return page < Math.ceil((double) totalAssets / pageSize);
    }

    private boolean hasNextPage(HistoricalSupply firstIndexSupply) {
        if (    isNull(firstIndexSupply.getSupplyCirculating()) &&
                isNull(firstIndexSupply.getSupplyTotal())       &&
                isNull(firstIndexSupply.getSupplyBurnt())       &&
                isNull(firstIndexSupply.getSupplyMax())         &&
                isNull(firstIndexSupply.getSupplyStaked())      &&
                isNull(firstIndexSupply.getSupplyFuture())      &&
                isNull(firstIndexSupply.getSupplyIssued())      &&
                isNull(firstIndexSupply.getSupplyLocked())
        ) {
            return false;
        }
        return true;
    }

}
