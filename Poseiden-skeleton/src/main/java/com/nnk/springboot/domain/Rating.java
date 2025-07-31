package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La notation Moody's est obligatoire")
    @Size(max = 125, message = "La notation Moody's ne peut pas dépasser 125 caractères")
    private String moodysRating;

    @NotBlank(message = "La notation S&P est obligatoire")
    @Size(max = 125, message = "La notation S&P ne peut pas dépasser 125 caractères")
    private String sandPRating;

    @NotBlank(message = "La notation Fitch est obligatoire")
    @Size(max = 125, message = "La notation Fitch ne peut pas dépasser 125 caractères")
    private String fitchRating;

    /**
     * Numéro d'ordre (doit être un nombre positif).
     */
    @Positive(message = "Le numéro d'ordre doit être un nombre positif.")
    private Integer orderNumber;
}
