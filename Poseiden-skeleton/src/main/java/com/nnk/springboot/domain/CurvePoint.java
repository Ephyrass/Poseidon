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
     * Identifiant de la courbe (doit être un nombre positif).
     */
    @NotNull(message = "L'identifiant de la courbe est obligatoire")
    @Positive(message = "L'identifiant de la courbe doit être un nombre positif.")
    private Integer curveId;

    private Timestamp asOfDate;

    /**
     * Terme de la courbe (doit être un nombre positif).
     */
    @Positive(message = "Le terme de la courbe doit être un nombre positif.")
    private Double term;

    /**
     * Valeur de la courbe (doit être un nombre positif).
     */
    @Positive(message = "La valeur de la courbe doit être un nombre positif.")
    @Column(name = "`value`")
    private Double value;

    private Timestamp creationDate;
}
