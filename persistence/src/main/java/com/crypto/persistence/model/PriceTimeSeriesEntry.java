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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_time", columnList = "time"),
                @Index(name = "idx_base_symbol", columnList = "base_symbol"),
                @Index(name = "idx_exchange", columnList = "exchange")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "baseSymbol", "exchange", "quoteSymbol", "time", "intervalType" })
        }
)
public class PriceTimeSeriesEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_symbol")
    private Coin baseSymbol;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange")
    private Exchange exchange;

    @NotBlank
    private String quoteSymbol;

    @NotNull
    @Enumerated(EnumType.STRING)
    private IntervalType intervalType;

    @NotNull
    private ZonedDateTime time;

    @NotNull
    private Double close;

    @NotNull
    private Double high;

    @NotNull
    private Double low;

    @NotNull
    private Double open;

    @NotNull
    private Double volume;
}
