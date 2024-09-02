package com.crypto.analytics.composer.entity;

import com.crypto.analytics.mapper.entity.ExchangeMapper;
import com.crypto.cryptocompare.api.data.response.ExchangeData;
import com.crypto.persistence.model.Exchange;
import com.crypto.persistence.model.ExchangeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeComposer {

    private final ExchangeMapper mapper;

    public Exchange fromExchangeData(String name, ExchangeData data, ExchangeType type) {
        Exchange exchange = mapper.fromExchangeData(data, type);
        exchange.setName(name);
        return exchange;
    }

    public List<Exchange> fromExchangeDataMap(Map<String, ? extends ExchangeData> exchangeNameExchangeData, ExchangeType type) {
        return exchangeNameExchangeData.entrySet()
                                       .stream()
                                       .map(entry -> fromExchangeData(entry.getKey(), entry.getValue(), type))
                                       .toList();
    }}
