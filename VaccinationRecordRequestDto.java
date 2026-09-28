package com.example.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecordRequestDto {

    @NotNull(message = "Pet ID is required")
    private Long petId;

    // Can be identified by vaccineTypeId or vaccineName
    private Long vaccineTypeId;

    private String vaccineName;

    @NotNull(message = "Date administered is required")
    @PastOrPresent(message = "Vaccination date cannot be in the future")
    private LocalDate dateAdministered;

    private String administeredBy;

    private String batchNumber;

    private String notes;
}
