package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "trade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TradeId")
    private Integer tradeId;

    @NotBlank(message = "Le compte est obligatoire")
    @Size(max = 30, message = "Le compte ne peut pas dépasser 30 caractères")
    private String account;

    @NotBlank(message = "Le type est obligatoire")
    @Size(max = 30, message = "Le type ne peut pas dépasser 30 caractères")
    private String type;

    /**
     * Quantité achetée (doit être un nombre positif).
     */
    @Positive(message = "La quantité achetée doit être un nombre positif.")
    private Double buyQuantity;

    /**
     * Quantité vendue (doit être un nombre positif).
     */
    @Positive(message = "La quantité vendue doit être un nombre positif.")
    private Double sellQuantity;

    /**
     * Prix d'achat (doit être un nombre positif).
     */
    @Positive(message = "Le prix d'achat doit être un nombre positif.")
    private Double buyPrice;

    /**
     * Prix de vente (doit être un nombre positif).
     */
    @Positive(message = "Le prix de vente doit être un nombre positif.")
    private Double sellPrice;

    @Size(max = 125, message = "Le benchmark ne peut pas dépasser 125 caractères")
    private String benchmark;

    private Timestamp tradeDate;

    @Size(max = 125, message = "La sécurité ne peut pas dépasser 125 caractères")
    private String security;

    @Size(max = 10, message = "Le statut ne peut pas dépasser 10 caractères")
    private String status;

    @Size(max = 125, message = "Le trader ne peut pas dépasser 125 caractères")
    private String trader;

    @Size(max = 125, message = "Le book ne peut pas dépasser 125 caractères")
    private String book;

    @Size(max = 125, message = "Le nom de création ne peut pas dépasser 125 caractères")
    private String creationName;

    private Timestamp creationDate;

    @Size(max = 125, message = "Le nom de révision ne peut pas dépasser 125 caractères")
    private String revisionName;

    private Timestamp revisionDate;

    @Size(max = 125, message = "Le nom de deal ne peut pas dépasser 125 caractères")
    private String dealName;

    @Size(max = 125, message = "Le type de deal ne peut pas dépasser 125 caractères")
    private String dealType;

    @Size(max = 125, message = "L'ID de liste source ne peut pas dépasser 125 caractères")
    private String sourceListId;

    @Size(max = 125, message = "Le côté ne peut pas dépasser 125 caractères")
    private String side;
}
