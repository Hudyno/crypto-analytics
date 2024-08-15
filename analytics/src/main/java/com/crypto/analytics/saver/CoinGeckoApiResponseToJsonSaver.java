package com.crypto.analytics.saver;

import com.crypto.analytics.config.CoinGeckoConfig;
import com.crypto.analytics.util.CollectionUtil;
import com.crypto.analytics.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.litesoftwares.coingecko.domain.Coins.CoinList;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoinGeckoApiResponseToJsonSaver extends ApiResponseToJsonSaver {

    private final CoinGeckoApiClientImpl coinGeckoApiClientImpl;

    public CoinGeckoApiResponseToJsonSaver(JsonUtil jsonUtil, CollectionUtil collectionUtil,
                                           CoinGeckoApiClientImpl coinGeckoApiClientImpl, CoinGeckoConfig coinGeckoConfig) {
        super(jsonUtil, collectionUtil, coinGeckoConfig.getRateLimit(), coinGeckoConfig.getRefreshRate());
        this.coinGeckoApiClientImpl = coinGeckoApiClientImpl;
    }

    public void saveCoinList() {
        this.saveApiResponse(coinGeckoApiClientImpl::getCoinList, "test.json");
    }

    public void saveAllCoinById(String fileName) {
        List<CoinList> coinList = jsonUtil.readJson(new File("coin_id_list.json"), new TypeReference<>(){});
        List<String> idList = coinList.stream().map(CoinList::getId).toList();
        coinList.clear();

        this.batchSaveApiResponse(
                (subList, params) -> coinGeckoApiClientImpl.getCoinById((List<String>) subList),
                fileName,
                idList
        );
    }
}
