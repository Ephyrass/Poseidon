package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bidlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BidListId")
    private Integer id;

    @NotBlank(message = "Account is required")
    @Size(max = 30, message = "Account cannot exceed 30 characters")
    private String account;

    @NotBlank(message = "Type is required")
    @Size(max = 30, message = "Type cannot exceed 30 characters")
    private String type;

    /**
     * Bid quantity (must be a positive number).
     */
    @Positive(message = "Bid quantity must be a positive number.")
    private Double bidQuantity;

    /**
     * Ask quantity (must be a positive number).
     */
    @Positive(message = "Ask quantity must be a positive number.")
    private Double askQuantity;

    /**
     * Bid price (must be a positive number).
     */
    @Positive(message = "Bid price must be a positive number.")
    private Double bid;

    /**
     * Ask price (must be a positive number).
     */
    @Positive(message = "Ask price must be a positive number.")
    private Double ask;

    @Size(max = 125, message = "Benchmark cannot exceed 125 characters")
    private String benchmark;

    private Timestamp bidListDate;

    @Size(max = 125, message = "Commentary cannot exceed 125 characters")
    private String commentary;

    @Size(max = 125, message = "Security cannot exceed 125 characters")
    private String security;

    @Size(max = 10, message = "Status cannot exceed 10 characters")
    private String status;

    @Size(max = 125, message = "Trader cannot exceed 125 characters")
    private String trader;

    @Size(max = 125, message = "Book cannot exceed 125 characters")
    private String book;

    @Size(max = 125, message = "Creation name cannot exceed 125 characters")
    private String creationName;

    private Timestamp creationDate;

    @Size(max = 125, message = "Revision name cannot exceed 125 characters")
    private String revisionName;

    private Timestamp revisionDate;

    @Size(max = 125, message = "Deal name cannot exceed 125 characters")
    private String dealName;

    @Size(max = 125, message = "Deal type cannot exceed 125 characters")
    private String dealType;

    @Size(max = 125, message = "Source list ID cannot exceed 125 characters")
    private String sourceListId;

    @Size(max = 125, message = "Side cannot exceed 125 characters")
    private String side;
}