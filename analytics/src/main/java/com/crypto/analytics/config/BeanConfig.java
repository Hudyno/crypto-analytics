package com.crypto.analytics.config;

import com.litesoftwares.coingecko.constant.TokenType;
import com.litesoftwares.coingecko.domain.ApiToken;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final CoinGeckoConfig coinGeckoConfig;

    @Bean
    public CoinGeckoApiClientImpl coinGeckoApiClientImpl() {
        return new CoinGeckoApiClientImpl(new ApiToken(TokenType.DEMO, coinGeckoConfig.getKey()));
    }
}
