package com.crypto.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_stat_base_symbol", columnList = "base_symbol"),
                @Index(name = "idx_stat_to_symbol", columnList = "to_symbol"),
                @Index(name = "idx_stat_period", columnList = "period")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "baseSymbol", "toSymbol", "interval", "period" })
        }
)
public class StatisticalRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_symbol")
    @NotNull
    private Coin baseSymbol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_symbol")
    @NotNull
    private Coin toSymbol;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private IntervalType interval;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Period period;

    private Double correlation;

    private Double beta;

    @UpdateTimestamp
    private Instant lastUpdated;
}
