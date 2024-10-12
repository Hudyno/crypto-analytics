package com.crypto.persistence.service;

import com.crypto.persistence.model.Coin;
import com.crypto.persistence.repository.CoinRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class CoinService extends BaseService<Coin, String> {

    private final CoinRepository coinRepository;

    public CoinService(JpaRepository<Coin, String> repository, CoinRepository coinRepository) {
        super(repository);
        this.coinRepository = coinRepository;
    }
}
