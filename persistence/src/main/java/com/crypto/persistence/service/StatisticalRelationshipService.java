package com.crypto.persistence.service;

import com.crypto.persistence.model.StatisticalRelationship;
import com.crypto.persistence.repository.StatisticalRelationshipRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class StatisticalRelationshipService extends BaseService<StatisticalRelationship, Long> {

    private final StatisticalRelationshipRepository repository;

    public StatisticalRelationshipService(JpaRepository<StatisticalRelationship, Long> jpaRepository, StatisticalRelationshipRepository repository) {
        super(jpaRepository);
        this.repository = repository;
    }
}
