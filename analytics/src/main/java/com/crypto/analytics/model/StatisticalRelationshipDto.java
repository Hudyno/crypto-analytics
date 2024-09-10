package com.crypto.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticalRelationshipDto {

    private Double correlation;

    private Double beta;
}
