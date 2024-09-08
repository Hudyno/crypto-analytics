package com.crypto.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
public class DescriptiveStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Metrics metrics;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private IntervalType interval;

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
