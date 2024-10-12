package com.crypto.persistence.model;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CoinMetrics {

    @Id
    private String id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Coin coin;

    private BigDecimal supplyMax;

    private BigDecimal supplyIssued;

    private BigDecimal supplyTotal;

    private BigDecimal supplyCirculating;

    private BigDecimal supplyFuture;

    private BigDecimal supplyLocked;

    private BigDecimal supplyBurnt;

    private BigDecimal supplyStaked;
}
