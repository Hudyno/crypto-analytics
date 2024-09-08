package com.crypto.persistence.repository;

import com.crypto.persistence.model.DescriptiveStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptiveStatisticsRepository extends JpaRepository<DescriptiveStatistics, String> {
}
