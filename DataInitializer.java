package com.example.project.config;

import com.example.project.entity.Owner;
import com.example.project.entity.Pet;
import com.example.project.entity.VaccinationRecord;
import com.example.project.entity.VaccineType;
import com.example.project.repository.OwnerRepository;
import com.example.project.repository.PetRepository;
import com.example.project.repository.VaccinationRecordRepository;
import com.example.project.repository.VaccineTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final VaccineTypeRepository vaccineTypeRepository;
    private final VaccinationRecordRepository vaccinationRecordRepository;

    @Override
    public void run(String... args) {
        if (vaccineTypeRepository.count() > 0) {
            log.info("Database already seeded with demo records.");
            return;
        }

        log.info("Seeding initial demo data for PetCareBook...");

        // 1. Standard Vaccine Types
        VaccineType rabies = vaccineTypeRepository.save(VaccineType.builder()
                .name("Rabies (Canine & Feline)")
                .targetSpecies("All")
                .description("Core annual vaccine preventing rabies virus transmission.")
                .standardIntervalDays(365)
                .mandatory(true)
                .build());

        VaccineType dhpp = vaccineTypeRepository.save(VaccineType.builder()
                .name("DHPP (Distemper, Parvo, Hepatitis)")
                .targetSpecies("Dog")
                .description("Core canine combination vaccine protecting against fatal viral infections.")
                .standardIntervalDays(365)
                .mandatory(true)
                .build());

        VaccineType bordetella = vaccineTypeRepository.save(VaccineType.builder()
                .name("Bordetella (Kennel Cough)")
                .targetSpecies("Dog")
                .description("Recommended bi-annual protection for dogs in boarding, day care or social parks.")
                .standardIntervalDays(180)
                .mandatory(false)
                .build());

        VaccineType deworming = vaccineTypeRepository.save(VaccineType.builder()
                .name("Canine Broad-Spectrum Deworming")
                .targetSpecies("Dog")
                .description("Quarterly internal parasite and intestinal worm control.")
                .standardIntervalDays(90)
                .mandatory(false)
                .build());

        VaccineType fvrcp = vaccineTypeRepository.save(VaccineType.builder()
                .name("FVRCP (Feline Core)")
                .targetSpecies("Cat")
                .description("Core feline protection against Rhinotracheitis, Calicivirus, and Panleukopenia.")
                .standardIntervalDays(365)
                .mandatory(true)
                .build());

        VaccineType felv = vaccineTypeRepository.save(VaccineType.builder()
                .name("Feline Leukemia Virus (FeLV)")
                .targetSpecies("Cat")
                .description("Recommended for outdoor cats or cats living in multi-cat environments.")
                .standardIntervalDays(365)
                .mandatory(false)
                .build());

        // 2. Sample Owners
        Owner sarah = ownerRepository.save(Owner.builder()
                .fullName("Sarah Connor")
                .email("sarah.connor@example.com")
                .phoneNumber("+1 (555) 234-5678")
                .address("742 Evergreen Terrace, Springfield")
                .build());

        Owner david = ownerRepository.save(Owner.builder()
                .fullName("David Miller")
                .email("david.miller@example.com")
                .phoneNumber("+1 (555) 876-5432")
                .address("221B Baker Street, London")
                .build());

        Owner emma = ownerRepository.save(Owner.builder()
                .fullName("Emma Watson")
                .email("emma.watson@example.com")
                .phoneNumber("+1 (555) 432-1098")
                .address("4 Privet Drive, Little Whinging")
                .build());

        // 3. Sample Pets
        Pet milo = petRepository.save(Pet.builder()
                .name("Milo")
                .species("Dog")
                .breed("Golden Retriever")
                .dateOfBirth(LocalDate.now().minusYears(2).minusMonths(3))
                .gender("Male")
                .weight(31.5)
                .owner(sarah)
                .build());

        Pet luna = petRepository.save(Pet.builder()
                .name("Luna")
                .species("Cat")
                .breed("Siamese")
                .dateOfBirth(LocalDate.now().minusYears(1).minusMonths(6))
                .gender("Female")
                .weight(4.2)
                .owner(david)
                .build());

        Pet charlie = petRepository.save(Pet.builder()
                .name("Charlie")
                .species("Dog")
                .breed("Beagle")
                .dateOfBirth(LocalDate.now().minusYears(3).minusMonths(1))
                .gender("Male")
                .weight(14.0)
                .owner(sarah)
                .build());

        Pet oliver = petRepository.save(Pet.builder()
                .name("Oliver")
                .species("Cat")
                .breed("Tabby")
                .dateOfBirth(LocalDate.now().minusMonths(9))
                .gender("Male")
                .weight(3.8)
                .owner(emma)
                .build());

        Pet coco = petRepository.save(Pet.builder()
                .name("Coco")
                .species("Dog")
                .breed("Poodle")
                .dateOfBirth(LocalDate.now().minusYears(1).minusMonths(2))
                .gender("Female")
                .weight(7.4)
                .owner(emma)
                .build());

        // 4. Vaccination Records
        // Due Soon (Due in 5 days): Milo DHPP
        LocalDate date1 = LocalDate.now().minusDays(360);
        vaccinationRecordRepository.save(VaccinationRecord.builder()
                .pet(milo)
                .vaccineType(dhpp)
                .dateAdministered(date1)
                .nextDueDate(date1.plusDays(dhpp.getStandardIntervalDays())) // today + 5 days -> DUE IN NEXT 7 DAYS
                .administeredBy("Green Valley Animal Hospital")
                .batchNumber("DHPP-2025-081")
                .notes("Annual booster scheduled. Pet in great health.")
                .build());

        // Due Soon (Due in 3 days): Luna FVRCP
        LocalDate date2 = LocalDate.now().minusDays(362);
        vaccinationRecordRepository.save(VaccinationRecord.builder()
                .pet(luna)
                .vaccineType(fvrcp)
                .dateAdministered(date2)
                .nextDueDate(date2.plusDays(fvrcp.getStandardIntervalDays())) // today + 3 days -> DUE IN NEXT 7 DAYS
                .administeredBy("City Feline Wellness Clinic")
                .batchNumber("FVRCP-993-B")
                .notes("Healthy coat and clear eyes.")
                .build());

        // Due Soon (Due in 5 days): Coco Deworming
        LocalDate date3 = LocalDate.now().minusDays(85);
        vaccinationRecordRepository.save(VaccinationRecord.builder()
                .pet(coco)
                .vaccineType(deworming)
                .dateAdministered(date3)
                .nextDueDate(date3.plusDays(deworming.getStandardIntervalDays())) // today + 5 days -> DUE IN NEXT 7 DAYS
                .administeredBy("Springfield Vet Care")
                .batchNumber("DWM-441-A")
                .notes("Routine preventive deworming administered.")
                .build());

        // Overdue (Due 10 days ago): Charlie Bordetella
        LocalDate date4 = LocalDate.now().minusDays(190);
        vaccinationRecordRepository.save(VaccinationRecord.builder()
                .pet(charlie)
                .vaccineType(bordetella)
                .dateAdministered(date4)
                .nextDueDate(date4.plusDays(bordetella.getStandardIntervalDays())) // today - 10 days -> OVERDUE
                .administeredBy("Happy Paws Veterinary")
                .batchNumber("BORD-1092")
                .notes("Boarding requirement; needs urgent renewal.")
                .build());

        // Up to date: Milo Rabies (given 60 days ago)
        LocalDate date5 = LocalDate.now().minusDays(60);
        vaccinationRecordRepository.save(VaccinationRecord.builder()
                .pet(milo)
                .vaccineType(rabies)
                .dateAdministered(date5)
                .nextDueDate(date5.plusDays(rabies.getStandardIntervalDays()))
                .administeredBy("Green Valley Animal Hospital")
                .batchNumber("RAB-2026-X1")
                .notes("Rabies tag #4492 issued.")
                .build());

        // Up to date: Oliver FVRCP (given 30 days ago)
        LocalDate date6 = LocalDate.now().minusDays(30);
        vaccinationRecordRepository.save(VaccinationRecord.builder()
                .pet(oliver)
                .vaccineType(fvrcp)
                .dateAdministered(date6)
                .nextDueDate(date6.plusDays(fvrcp.getStandardIntervalDays()))
                .administeredBy("Springfield Vet Care")
                .batchNumber("FVRCP-102-K")
                .notes("Kitten initial vaccination complete.")
                .build());

        log.info("PetCareBook demo data seeding completed successfully.");
    }
}
