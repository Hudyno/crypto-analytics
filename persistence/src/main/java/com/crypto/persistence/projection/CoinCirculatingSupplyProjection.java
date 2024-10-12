package com.crypto.persistence.projection;


import java.math.BigDecimal;
import java.time.ZonedDateTime;


public interface CoinCirculatingSupplyProjection {

    String getBaseSymbolSymbol();
    ZonedDateTime getTime();
    BigDecimal getSupplyCirculating();
}
