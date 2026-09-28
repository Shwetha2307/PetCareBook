package com.example.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vaccination_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull(message = "Vaccination record must be associated with a pet")
    private Pet pet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vaccine_type_id", nullable = false)
    @NotNull(message = "Vaccine type must be specified")
    private VaccineType vaccineType;

    @NotNull(message = "Date administered is required")
    @PastOrPresent(message = "Vaccination date cannot be in the future")
    @Column(nullable = false)
    private LocalDate dateAdministered;

    @NotNull(message = "Next due date must be computed")
    @Column(nullable = false)
    private LocalDate nextDueDate;

    private String administeredBy;

    private String batchNumber;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
