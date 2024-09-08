package com.crypto.persistence.projection;


import java.time.ZonedDateTime;


public interface CoinClosingPriceProjection {

    String getBaseSymbolSymbol();
    ZonedDateTime getTime();
    Double getClose();
}
