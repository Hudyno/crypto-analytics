package com.crypto.persistence.service;

import com.crypto.persistence.model.DescriptiveStatistics;
import com.crypto.persistence.repository.DescriptiveStatisticsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DescriptiveStatisticsService extends BaseService<DescriptiveStatistics, String> {

    private final DescriptiveStatisticsRepository descriptiveStatisticsRepository;

    public DescriptiveStatisticsService(JpaRepository<DescriptiveStatistics, String> repository, DescriptiveStatisticsRepository descriptiveStatisticsRepository) {
        super(repository);
        this.descriptiveStatisticsRepository = descriptiveStatisticsRepository;
    }
}
