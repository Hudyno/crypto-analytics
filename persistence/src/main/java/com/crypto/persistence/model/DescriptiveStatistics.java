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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_symbol", columnList = "symbol"),
                @Index(name = "idx_period", columnList = "period"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "symbol", "interval", "period" })
        }
)
public class DescriptiveStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol")
    private Coin symbol;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private IntervalType interval;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Period period;

    private Double mean;

    private Double median;

    private Double standardDeviation;

    private Double kurtosis;

    private Double skewness;

    private Double minimum;

    private Double maximum;

    private Long count;

    @UpdateTimestamp
    private Instant lastUpdated;
}
