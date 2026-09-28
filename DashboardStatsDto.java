package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalPets;
    private long totalOwners;
    private long totalVaccineTypes;
    private long totalVaccinationsGiven;
    private long dueInNext7DaysCount;
    private long overdueCount;
}
