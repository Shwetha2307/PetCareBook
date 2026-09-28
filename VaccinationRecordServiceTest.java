package com.example.project;

import com.example.project.dto.VaccinationRecordRequestDto;
import com.example.project.dto.VaccinationRecordResponseDto;
import com.example.project.entity.Owner;
import com.example.project.entity.Pet;
import com.example.project.entity.VaccinationRecord;
import com.example.project.entity.VaccineType;
import com.example.project.exception.BusinessValidationException;
import com.example.project.repository.VaccinationRecordRepository;
import com.example.project.service.PetService;
import com.example.project.service.VaccinationRecordService;
import com.example.project.service.VaccineTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccinationRecordServiceTest {

    @Mock
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Mock
    private PetService petService;

    @Mock
    private VaccineTypeService vaccineTypeService;

    @InjectMocks
    private VaccinationRecordService vaccinationRecordService;

    private Pet testPet;
    private VaccineType testVaccineType;

    @BeforeEach
    void setUp() {
        Owner owner = Owner.builder()
                .id(1L)
                .fullName("Alice Smith")
                .email("alice@example.com")
                .phoneNumber("555-1122")
                .build();

        testPet = Pet.builder()
                .id(10L)
                .name("Max")
                .species("Dog")
                .breed("Labrador")
                .dateOfBirth(LocalDate.now().minusYears(2))
                .owner(owner)
                .build();

        testVaccineType = VaccineType.builder()
                .id(100L)
                .name("Rabies (Annual)")
                .targetSpecies("Dog")
                .standardIntervalDays(365)
                .build();
    }

    @Test
    @DisplayName("Should successfully log vaccination and auto-calculate next due date correctly")
    void testLogVaccinationSuccess() {
        LocalDate administeredDate = LocalDate.now().minusDays(10);
        LocalDate expectedDueDate = administeredDate.plusDays(365);

        VaccinationRecordRequestDto request = VaccinationRecordRequestDto.builder()
                .petId(10L)
                .vaccineTypeId(100L)
                .dateAdministered(administeredDate)
                .administeredBy("Dr. Goodvet")
                .batchNumber("BATCH-123")
                .build();

        when(petService.getPetEntity(10L)).thenReturn(testPet);
        when(vaccineTypeService.getVaccineTypeEntity(100L)).thenReturn(testVaccineType);
        when(vaccinationRecordRepository.save(any(VaccinationRecord.class))).thenAnswer(invocation -> {
            VaccinationRecord record = invocation.getArgument(0);
            record.setId(1L);
            return record;
        });

        VaccinationRecordResponseDto response = vaccinationRecordService.logVaccination(request);

        assertNotNull(response);
        assertEquals(10L, response.getPetId());
        assertEquals("Max", response.getPetName());
        assertEquals("Rabies (Annual)", response.getVaccineName());
        assertEquals(administeredDate, response.getDateAdministered());
        assertEquals(expectedDueDate, response.getNextDueDate());
        verify(vaccinationRecordRepository, times(1)).save(any(VaccinationRecord.class));
    }

    @Test
    @DisplayName("Business Rule: Should reject vaccination logged with a future date")
    void testRejectFutureVaccinationDate() {
        LocalDate futureDate = LocalDate.now().plusDays(2);

        VaccinationRecordRequestDto request = VaccinationRecordRequestDto.builder()
                .petId(10L)
                .vaccineTypeId(100L)
                .dateAdministered(futureDate)
                .build();

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> {
            vaccinationRecordService.logVaccination(request);
        });

        assertTrue(ex.getMessage().contains("future date"));
        verifyNoInteractions(vaccinationRecordRepository);
    }

    @Test
    @DisplayName("Business Rule: Should reject vaccination date prior to pet birth date")
    void testRejectVaccinationBeforePetBirthDate() {
        LocalDate dateBeforeBirth = testPet.getDateOfBirth().minusMonths(1);

        VaccinationRecordRequestDto request = VaccinationRecordRequestDto.builder()
                .petId(10L)
                .vaccineTypeId(100L)
                .dateAdministered(dateBeforeBirth)
                .build();

        when(petService.getPetEntity(10L)).thenReturn(testPet);

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> {
            vaccinationRecordService.logVaccination(request);
        });

        assertTrue(ex.getMessage().contains("cannot precede pet's date of birth"));
        verify(vaccinationRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Business Rule: Should reject when vaccine type has missing or invalid interval")
    void testRejectInvalidStandardInterval() {
        testVaccineType.setStandardIntervalDays(0);

        VaccinationRecordRequestDto request = VaccinationRecordRequestDto.builder()
                .petId(10L)
                .vaccineTypeId(100L)
                .dateAdministered(LocalDate.now().minusDays(5))
                .build();

        when(petService.getPetEntity(10L)).thenReturn(testPet);
        when(vaccineTypeService.getVaccineTypeEntity(100L)).thenReturn(testVaccineType);

        BusinessValidationException ex = assertThrows(BusinessValidationException.class, () -> {
            vaccinationRecordService.logVaccination(request);
        });

        assertTrue(ex.getMessage().contains("standard interval"));
        verify(vaccinationRecordRepository, never()).save(any());
    }
}
