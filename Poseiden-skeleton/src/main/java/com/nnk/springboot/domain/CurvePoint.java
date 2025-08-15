package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "curvepoint")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurvePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Curve identifier (must be a positive number).
     */
    @NotNull(message = "Curve identifier is required")
    @Positive(message = "Curve identifier must be a positive number.")
    private Integer curveId;

    private Timestamp asOfDate;

    /**
     * Curve term (must be a positive number).
     */
    @Positive(message = "Curve term must be a positive number.")
    private Double term;

    /**
     * Curve value (must be a positive number).
     */
    @Positive(message = "Curve value must be a positive number.")
    @Column(name = "`value`")
    private Double value;

    private Timestamp creationDate;
}
