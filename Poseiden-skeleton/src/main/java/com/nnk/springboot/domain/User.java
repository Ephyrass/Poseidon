package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nom d'utilisateur unique.
     */
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    /**
     * Mot de passe de l'utilisateur (doit respecter les critères de sécurité).
     * Au moins 8 caractères, une majuscule, un chiffre et un symbole.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?]).*$",
        message = "Le mot de passe doit contenir au moins une lettre majuscule, un chiffre et un symbole"
    )
    private String password;

    /**
     * Nom complet de l'utilisateur.
     */
    @NotBlank(message = "Le nom complet est obligatoire")
    @Size(max = 100, message = "Le nom complet ne peut pas dépasser 100 caractères")
    private String fullname;

    /**
     * Rôle de l'utilisateur (ex. : ADMIN, USER).
     */
    @NotBlank(message = "Le rôle est obligatoire")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Le rôle doit être ADMIN ou USER")
    private String role;
}
