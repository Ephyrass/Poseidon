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

    @NotBlank(message = "Rule name is required")
    @Size(max = 125, message = "Rule name cannot exceed 125 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 125, message = "Description cannot exceed 125 characters")
    private String description;

    @NotBlank(message = "JSON is required")
    @Size(max = 125, message = "JSON cannot exceed 125 characters")
    private String json;

    @NotBlank(message = "Template is required")
    @Size(max = 512, message = "Template cannot exceed 512 characters")
    private String template;

    @NotBlank(message = "SQL query is required")
    @Size(max = 125, message = "SQL query cannot exceed 125 characters")
    private String sqlStr;

    @NotBlank(message = "SQL part is required")
    @Size(max = 125, message = "SQL part cannot exceed 125 characters")
    private String sqlPart;
}
