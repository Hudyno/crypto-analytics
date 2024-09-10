package com.crypto.persistence.repository;

import com.crypto.persistence.model.StatisticalRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticalRelationshipRepository extends JpaRepository<StatisticalRelationship, Long> {
}
