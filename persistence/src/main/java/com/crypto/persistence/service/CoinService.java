package com.crypto.persistence.service;

import com.crypto.persistence.model.Coin;
import com.crypto.persistence.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class CoinService {

    private final CoinRepository coinRepository;

    public void saveAll(List<Coin> coinList) {
        if (CollectionUtils.isEmpty(coinList)) {
            return;
        }

        List<Coin> cleanedCoinList = coinList.stream().filter(Objects::nonNull).toList();
        if (coinList.size() != cleanedCoinList.size()) {
            log.info("filtered out {} null objects", coinList.size() - cleanedCoinList.size());
        }

        this.coinRepository.saveAll(coinList);
        log.info("saved {} objects", cleanedCoinList.size());
    }
}
