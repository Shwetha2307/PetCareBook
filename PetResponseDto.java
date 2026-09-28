package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponseDto {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate dateOfBirth;
    private Integer ageYears;
    private Integer ageMonths;
    private String gender;
    private Double weight;
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;
    private int vaccinationRecordCount;
    private LocalDateTime createdAt;
}
