package com.example.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccineTypeRequestDto {

    @NotBlank(message = "Vaccine name is required")
    private String name;

    @NotBlank(message = "Target species is required (e.g. Dog, Cat, All)")
    private String targetSpecies;

    private String description;

    @NotNull(message = "Standard interval is required")
    @Positive(message = "Standard interval must be greater than 0 days")
    private Integer standardIntervalDays;

    @Builder.Default
    private Boolean mandatory = true;
}
