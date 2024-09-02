package com.crypto.persistence.service;

import com.crypto.persistence.model.Pair;
import com.crypto.persistence.repository.PairRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PairService extends BaseService<Pair, Long> {

    private final PairRepository pairRepository;

    public PairService(JpaRepository<Pair, Long> repository, PairRepository pairRepository) {
        super(repository);
        this.pairRepository = pairRepository;
    }
}
