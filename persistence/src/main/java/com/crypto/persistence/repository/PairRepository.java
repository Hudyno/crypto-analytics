package com.crypto.persistence.repository;

import com.crypto.persistence.model.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PairRepository extends JpaRepository<Pair, Long> {
}
