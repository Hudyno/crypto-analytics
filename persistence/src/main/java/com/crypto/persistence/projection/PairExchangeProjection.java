package com.crypto.persistence.projection;


import java.time.ZonedDateTime;


public interface PairExchangeProjection {

    String getBaseSymbol();
    String getQuoteSymbol();
    String getExchangeName();
    ZonedDateTime getFirstTradeTime();
    ZonedDateTime getLastTradeTime();
    Long getTotalTrades();
}
