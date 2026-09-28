package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccineTypeResponseDto {
    private Long id;
    private String name;
    private String targetSpecies;
    private String description;
    private Integer standardIntervalDays;
    private Boolean mandatory;
    private LocalDateTime createdAt;
}
