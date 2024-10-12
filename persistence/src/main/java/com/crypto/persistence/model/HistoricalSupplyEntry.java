package com.crypto.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "historical_supply_time", columnList = "time"),
                @Index(name = "historical_supply_base_symbol", columnList = "baseSymbol"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "baseSymbol", "time" })
        }
)
public class HistoricalSupplyEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_symbol")
    private Coin baseSymbol;

    private ZonedDateTime time;

    private BigDecimal supplyCirculating;

    private BigDecimal supplyTotal;

    private BigDecimal supplyBurnt;

    private BigDecimal supplyMax;

    private BigDecimal supplyStaked;

    private BigDecimal supplyFuture;

    private BigDecimal supplyIssued;

    private BigDecimal supplyLocked;
}
