package com.example.project.service;

import com.example.project.dto.DashboardStatsDto;
import com.example.project.repository.OwnerRepository;
import com.example.project.repository.PetRepository;
import com.example.project.repository.VaccinationRecordRepository;
import com.example.project.repository.VaccineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final VaccineTypeRepository vaccineTypeRepository;
    private final VaccinationRecordRepository vaccinationRecordRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        long totalPets = petRepository.count();
        long totalOwners = ownerRepository.count();
        long totalVaccineTypes = vaccineTypeRepository.count();
        long totalVaccinationsGiven = vaccinationRecordRepository.count();
        long dueInNext7DaysCount = vaccinationRecordRepository.countByNextDueDateBetween(today, nextWeek);
        long overdueCount = vaccinationRecordRepository.countByNextDueDateBefore(today);

        return DashboardStatsDto.builder()
                .totalPets(totalPets)
                .totalOwners(totalOwners)
                .totalVaccineTypes(totalVaccineTypes)
                .totalVaccinationsGiven(totalVaccinationsGiven)
                .dueInNext7DaysCount(dueInNext7DaysCount)
                .overdueCount(overdueCount)
                .build();
    }
}
