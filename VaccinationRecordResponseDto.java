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
public class VaccinationRecordResponseDto {
    private Long id;
    
    // Pet summary
    private Long petId;
    private String petName;
    private String species;
    private String breed;
    private LocalDate petDateOfBirth;
    
    // Owner summary
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;

    // Vaccine details
    private Long vaccineTypeId;
    private String vaccineName;
    private Integer standardIntervalDays;

    // Dates & Status
    private LocalDate dateAdministered;
    private LocalDate nextDueDate;
    private Long daysRemainingUntilDue;
    private String status; // "OVERDUE", "DUE_SOON", "UP_TO_DATE"

    private String administeredBy;
    private String batchNumber;
    private String notes;
    private LocalDateTime createdAt;
}
