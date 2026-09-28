package com.example.project;

import com.example.project.dto.*;
import com.example.project.exception.BusinessValidationException;
import com.example.project.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetCareIntegrationTests {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private PetService petService;

    @Autowired
    private VaccineTypeService vaccineTypeService;

    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @Autowired
    private DashboardService dashboardService;

    @Test
    @DisplayName("End-to-End Workflow: Register Owner, Register Pet, Verify Rules, Log Vaccination, Due in 7 Days, and Pet History")
    void testCompletePetVaccinationWorkflow() {
        // 1. Register Owner
        OwnerRequestDto ownerDto = OwnerRequestDto.builder()
                .fullName("Jennifer Aniston")
                .email("jennifer." + System.currentTimeMillis() + "@example.com")
                .phoneNumber("+1 (555) 345-6789")
                .address("Beverly Hills, CA")
                .build();

        OwnerResponseDto ownerResponse = ownerService.registerOwner(ownerDto);
        assertNotNull(ownerResponse.getId());
        assertEquals("Jennifer Aniston", ownerResponse.getFullName());

        // 2. Register Pet
        PetRequestDto petDto = PetRequestDto.builder()
                .name("Cooper")
                .species("Dog")
                .breed("Golden Retriever")
                .dateOfBirth(LocalDate.now().minusYears(2))
                .ownerId(ownerResponse.getId())
                .gender("Male")
                .weight(28.5)
                .build();

        PetResponseDto petResponse = petService.registerPet(petDto);
        assertNotNull(petResponse.getId());
        assertEquals("Cooper", petResponse.getName());
        assertEquals("Dog", petResponse.getSpecies());
        assertEquals(2, petResponse.getAgeYears());

        // 3. Define Vaccine Type with fixed standard interval (180 days)
        VaccineTypeRequestDto vaccineDto = VaccineTypeRequestDto.builder()
                .name("Test Leptospirosis Booster " + System.currentTimeMillis())
                .targetSpecies("Dog")
                .standardIntervalDays(180)
                .description("Bi-annual bacterial protection")
                .mandatory(false)
                .build();

        VaccineTypeResponseDto vaccineResponse = vaccineTypeService.createVaccineType(vaccineDto);
        assertNotNull(vaccineResponse.getId());
        assertEquals(180, vaccineResponse.getStandardIntervalDays());

        // 4. Test Business Rule: Reject Future Date
        VaccinationRecordRequestDto futureRecord = VaccinationRecordRequestDto.builder()
                .petId(petResponse.getId())
                .vaccineTypeId(vaccineResponse.getId())
                .dateAdministered(LocalDate.now().plusDays(3)) // FUTURE DATE
                .administeredBy("Integrity Vet")
                .build();

        BusinessValidationException futureEx = assertThrows(BusinessValidationException.class, () -> {
            vaccinationRecordService.logVaccination(futureRecord);
        });
        assertTrue(futureEx.getMessage().contains("future date"));

        // 5. Test Business Rule: Reject date prior to pet date of birth
        VaccinationRecordRequestDto preBirthRecord = VaccinationRecordRequestDto.builder()
                .petId(petResponse.getId())
                .vaccineTypeId(vaccineResponse.getId())
                .dateAdministered(petResponse.getDateOfBirth().minusMonths(2))
                .build();

        BusinessValidationException preBirthEx = assertThrows(BusinessValidationException.class, () -> {
            vaccinationRecordService.logVaccination(preBirthRecord);
        });
        assertTrue(preBirthEx.getMessage().contains("cannot precede pet's date of birth"));

        // 6. Normal Path: Log valid past vaccination (Administered 176 days ago -> due in 4 days!)
        LocalDate administeredDate = LocalDate.now().minusDays(176);
        LocalDate expectedDueDate = administeredDate.plusDays(180);

        VaccinationRecordRequestDto validRecord = VaccinationRecordRequestDto.builder()
                .petId(petResponse.getId())
                .vaccineTypeId(vaccineResponse.getId())
                .dateAdministered(administeredDate)
                .administeredBy("Green Valley Animal Hospital")
                .batchNumber("LEPTO-883")
                .notes("Routine booster; healthy response.")
                .build();

        VaccinationRecordResponseDto recordResponse = vaccinationRecordService.logVaccination(validRecord);
        assertNotNull(recordResponse.getId());
        assertEquals(expectedDueDate, recordResponse.getNextDueDate());
        assertEquals("DUE_SOON", recordResponse.getStatus());
        assertEquals(4L, recordResponse.getDaysRemainingUntilDue());

        // 7. Verify List All Pets with Vaccinations Due in Next 7 Days
        List<VaccinationRecordResponseDto> dueSoonList = vaccinationRecordService.getVaccinationsDueInDays(7);
        assertFalse(dueSoonList.isEmpty());
        boolean foundCooper = dueSoonList.stream()
                .anyMatch(r -> r.getPetId().equals(petResponse.getId()) && r.getNextDueDate().equals(expectedDueDate));
        assertTrue(foundCooper, "Cooper's vaccination due in 4 days should be in the due-in-7-days list");

        // 8. Verify Owner can view Pet's Complete Vaccination History
        List<VaccinationRecordResponseDto> history = vaccinationRecordService.getPetVaccinationHistory(petResponse.getId());
        assertEquals(1, history.size());
        assertEquals("Cooper", history.get(0).getPetName());
        assertEquals(vaccineResponse.getName(), history.get(0).getVaccineName());
        assertEquals("LEPTO-883", history.get(0).getBatchNumber());

        // 9. Verify Dashboard Statistics
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        assertTrue(stats.getTotalPets() >= 1);
        assertTrue(stats.getTotalOwners() >= 1);
        assertTrue(stats.getTotalVaccineTypes() >= 1);
        assertTrue(stats.getTotalVaccinationsGiven() >= 1);
        assertTrue(stats.getDueInNext7DaysCount() >= 1);
    }
}
