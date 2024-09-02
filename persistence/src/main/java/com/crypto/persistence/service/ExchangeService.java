package com.crypto.persistence.service;

import com.crypto.persistence.model.Exchange;
import com.crypto.persistence.repository.ExchangeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ExchangeService extends BaseService<Exchange, String>{

    private final ExchangeRepository exchangeRepository;

    public ExchangeService(JpaRepository<Exchange, String> repository, ExchangeRepository exchangeRepository) {
        super(repository);
        this.exchangeRepository = exchangeRepository;
    }
}
