package com.crypto.persistence.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coin {

    @Id
    @NotBlank
    private String symbol;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private MarketCapTier tier;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection
    private Set<Sector> sectors;

    private ZonedDateTime createdOn;

    private ZonedDateTime updatedOn;

    private ZonedDateTime launchDate;

    private Double priceUsd;

    private Long mktCapPenalty;

    private BigDecimal circulatingMktCapUsd;

    private BigDecimal totalMktCapUsd;

    private BigDecimal spotMoving24HourChangeUsd;

    private BigDecimal spotMoving24HourChangePercentageUsd;

    private BigDecimal spotMoving7DayChangeUsd;

    private BigDecimal spotMoving7DayChangePercentageUsd;

    private BigDecimal spotMoving30DayChangeUsd;

    private BigDecimal spotMoving30DayChangePercentageUsd;
}
