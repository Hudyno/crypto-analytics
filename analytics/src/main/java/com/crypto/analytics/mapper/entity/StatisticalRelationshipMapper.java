package com.crypto.analytics.mapper.entity;

import com.crypto.analytics.dto.StatisticalRelationshipDto;
import com.crypto.persistence.model.StatisticalRelationship;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class StatisticalRelationshipMapper {

    public abstract StatisticalRelationship fromStatisticalRelationshipDto(StatisticalRelationshipDto dto);
}
