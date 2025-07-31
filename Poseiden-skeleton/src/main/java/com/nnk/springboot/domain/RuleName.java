package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rulename")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom de la règle est obligatoire")
    @Size(max = 125, message = "Le nom de la règle ne peut pas dépasser 125 caractères")
    private String name;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 125, message = "La description ne peut pas dépasser 125 caractères")
    private String description;

    @NotBlank(message = "Le JSON est obligatoire")
    @Size(max = 125, message = "Le JSON ne peut pas dépasser 125 caractères")
    private String json;

    @NotBlank(message = "Le template est obligatoire")
    @Size(max = 512, message = "Le template ne peut pas dépasser 512 caractères")
    private String template;

    @NotBlank(message = "La requête SQL est obligatoire")
    @Size(max = 125, message = "La requête SQL ne peut pas dépasser 125 caractères")
    private String sqlStr;

    @NotBlank(message = "La partie SQL est obligatoire")
    @Size(max = 125, message = "La partie SQL ne peut pas dépasser 125 caractères")
    private String sqlPart;
}
