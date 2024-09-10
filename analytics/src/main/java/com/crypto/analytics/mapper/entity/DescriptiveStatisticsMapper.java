package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.dto.DescriptiveStatisticsDto;
import com.crypto.persistence.model.DescriptiveStatistics;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class DescriptiveStatisticsMapper {

    public abstract DescriptiveStatistics fromDescriptiveStatisticsDto(DescriptiveStatisticsDto dto);
}
