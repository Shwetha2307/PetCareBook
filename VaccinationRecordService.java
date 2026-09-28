package com.example.project.service;

import com.example.project.dto.VaccinationRecordRequestDto;
import com.example.project.dto.VaccinationRecordResponseDto;
import com.example.project.entity.Pet;
import com.example.project.entity.VaccinationRecord;
import com.example.project.entity.VaccineType;
import com.example.project.exception.BusinessValidationException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.VaccinationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationRecordService {

    private final VaccinationRecordRepository vaccinationRecordRepository;
    private final PetService petService;
    private final VaccineTypeService vaccineTypeService;

    @Transactional
    public VaccinationRecordResponseDto logVaccination(VaccinationRecordRequestDto requestDto) {
        // Business Rule 1: Cannot log with future date
        if (requestDto.getDateAdministered().isAfter(LocalDate.now())) {
            throw new BusinessValidationException("A vaccination record cannot be logged with a future date.");
        }

        // Retrieve Pet
        Pet pet = petService.getPetEntity(requestDto.getPetId());

        // Business Rule: Vaccination cannot precede pet date of birth
        if (requestDto.getDateAdministered().isBefore(pet.getDateOfBirth())) {
            throw new BusinessValidationException("Vaccination date (" + requestDto.getDateAdministered() +
                    ") cannot precede pet's date of birth (" + pet.getDateOfBirth() + ").");
        }

        // Retrieve VaccineType by ID or Name
        VaccineType vaccineType;
        if (requestDto.getVaccineTypeId() != null) {
            vaccineType = vaccineTypeService.getVaccineTypeEntity(requestDto.getVaccineTypeId());
        } else if (requestDto.getVaccineName() != null && !requestDto.getVaccineName().trim().isEmpty()) {
            vaccineType = vaccineTypeService.getVaccineTypeByName(requestDto.getVaccineName().trim());
        } else {
            throw new BusinessValidationException("Vaccine type ID or Vaccine name must be provided.");
        }

        // Business Rule 2: Fixed interval must exist and be positive to compute next due date
        if (vaccineType.getStandardIntervalDays() == null || vaccineType.getStandardIntervalDays() <= 0) {
            throw new BusinessValidationException("Vaccine type '" + vaccineType.getName() +
                    "' must have a valid positive standard interval to compute the next due date.");
        }

        // Business Rule 3: Auto-calculate next due date
        LocalDate nextDueDate = requestDto.getDateAdministered().plusDays(vaccineType.getStandardIntervalDays());

        VaccinationRecord record = VaccinationRecord.builder()
                .pet(pet)
                .vaccineType(vaccineType)
                .dateAdministered(requestDto.getDateAdministered())
                .nextDueDate(nextDueDate)
                .administeredBy(requestDto.getAdministeredBy() != null ? requestDto.getAdministeredBy().trim() : "Veterinary Clinic")
                .batchNumber(requestDto.getBatchNumber() != null ? requestDto.getBatchNumber().trim() : null)
                .notes(requestDto.getNotes() != null ? requestDto.getNotes().trim() : null)
                .build();

        VaccinationRecord saved = vaccinationRecordRepository.save(record);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<VaccinationRecordResponseDto> getPetVaccinationHistory(Long petId) {
        // Verify pet exists
        petService.getPetEntity(petId);

        return vaccinationRecordRepository.findByPetIdOrderByDateAdministeredDesc(petId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VaccinationRecordResponseDto> getVaccinationsDueInDays(int days) {
        LocalDate today = LocalDate.now();
        LocalDate windowEnd = today.plusDays(days);

        return vaccinationRecordRepository.findByNextDueDateBetweenOrderByNextDueDateAsc(today, windowEnd)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VaccinationRecordResponseDto> getOverdueVaccinations() {
        LocalDate today = LocalDate.now();
        return vaccinationRecordRepository.findByNextDueDateBeforeOrderByNextDueDateAsc(today)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VaccinationRecordResponseDto> getAllVaccinations() {
        return vaccinationRecordRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VaccinationRecordResponseDto getVaccinationById(Long id) {
        VaccinationRecord record = vaccinationRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccination record not found with id: " + id));
        return mapToResponse(record);
    }

    @Transactional
    public void deleteVaccination(Long id) {
        if (!vaccinationRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vaccination record not found with id: " + id);
        }
        vaccinationRecordRepository.deleteById(id);
    }

    public VaccinationRecordResponseDto mapToResponse(VaccinationRecord record) {
        LocalDate today = LocalDate.now();
        long daysUntilDue = ChronoUnit.DAYS.between(today, record.getNextDueDate());

        String status;
        if (daysUntilDue < 0) {
            status = "OVERDUE";
        } else if (daysUntilDue <= 7) {
            status = "DUE_SOON";
        } else {
            status = "UP_TO_DATE";
        }

        Pet pet = record.getPet();
        VaccineType vt = record.getVaccineType();

        return VaccinationRecordResponseDto.builder()
                .id(record.getId())
                .petId(pet.getId())
                .petName(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .petDateOfBirth(pet.getDateOfBirth())
                .ownerId(pet.getOwner().getId())
                .ownerName(pet.getOwner().getFullName())
                .ownerEmail(pet.getOwner().getEmail())
                .ownerPhone(pet.getOwner().getPhoneNumber())
                .vaccineTypeId(vt.getId())
                .vaccineName(vt.getName())
                .standardIntervalDays(vt.getStandardIntervalDays())
                .dateAdministered(record.getDateAdministered())
                .nextDueDate(record.getNextDueDate())
                .daysRemainingUntilDue(daysUntilDue)
                .status(status)
                .administeredBy(record.getAdministeredBy())
                .batchNumber(record.getBatchNumber())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
