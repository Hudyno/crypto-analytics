package com.crypto.analytics.model.cryptocompare.min;

import com.crypto.cryptocompare.api.min.response.HistoricalOHLCV;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SymbolHistoricalOHLCV {

    @NonNull
    private String baseSymbol;

    @NonNull
    private String quoteSymbol;

    @NonNull
    private HistoricalOHLCV historicalOHLCV;
}
