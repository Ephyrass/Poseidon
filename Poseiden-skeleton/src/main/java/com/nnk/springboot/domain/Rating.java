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

    @NotBlank(message = "Moody's rating is required")
    @Size(max = 125, message = "Moody's rating cannot exceed 125 characters")
    private String moodysRating;

    @NotBlank(message = "S&P rating is required")
    @Size(max = 125, message = "S&P rating cannot exceed 125 characters")
    private String sandPRating;

    @NotBlank(message = "Fitch rating is required")
    @Size(max = 125, message = "Fitch rating cannot exceed 125 characters")
    private String fitchRating;

    /**
     * Order number (must be a positive number).
     */
    @Positive(message = "Order number must be a positive number.")
    private Integer orderNumber;
}
