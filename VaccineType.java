package com.example.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vaccine_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccineType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vaccine name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Target species is required (e.g., Dog, Cat, All)")
    @Column(nullable = false)
    private String targetSpecies;

    private String description;

    @NotNull(message = "Standard interval is required")
    @Positive(message = "Standard interval must be greater than 0 days")
    @Column(nullable = false)
    private Integer standardIntervalDays;

    @Builder.Default
    private Boolean mandatory = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
