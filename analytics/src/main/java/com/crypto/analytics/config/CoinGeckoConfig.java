package com.crypto.analytics.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "coingecko.api")
@Data
public class CoinGeckoConfig {

    private String key;
    private Integer rateLimit;
    private Integer refreshRate;
}
