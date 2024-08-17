package com.crypto.analytics.populate;

import com.crypto.analytics.mapper.CoinMapper;
import com.crypto.analytics.util.JsonUtil;
import com.crypto.cryptocompare.api.data.response.AssetSummary;
import com.crypto.cryptocompare.api.data.response.Response;
import com.crypto.persistence.model.Coin;
import com.crypto.persistence.service.CoinService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Log4j2
public class JsonToDatabasePopulate {

    private final CoinService coinService;
    private final CoinMapper coinMapper;
    private final JsonUtil jsonUtil;

    public void populateCoins(String fileName) {
        Response<AssetSummary> response = jsonUtil.readJson(new File(fileName), new TypeReference<>(){});

        if (isNull(response.getData()) || CollectionUtils.isEmpty(response.getData().getList())) {
            log.info("empty data in json response");
            return;
        }

        List<Coin> coinList = coinMapper.fromAssetSummary(response.getData().getList());
        coinService.saveAll(coinList);
    }
}
