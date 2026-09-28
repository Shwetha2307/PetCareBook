package com.example.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequestDto {

    @NotBlank(message = "Pet name is required")
    private String name;

    @NotBlank(message = "Species is required (e.g., Dog, Cat, Bird)")
    private String species;

    @NotBlank(message = "Breed is required")
    private String breed;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

    private String gender;

    @Positive(message = "Weight must be a positive number")
    private Double weight;

    @NotNull(message = "Owner ID is required to register a pet")
    private Long ownerId;
}
