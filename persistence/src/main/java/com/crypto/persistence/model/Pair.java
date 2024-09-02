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

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_exchange", columnList = "exchange")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "baseSymbol", "exchange", "quoteSymbol" })
        }
)
public class Pair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_name")
    @NotNull
    private Exchange exchange;

    @NotBlank
    private String baseSymbol;

    @NotBlank
    private String quoteSymbol;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
}
