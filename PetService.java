package com.example.project.service;

import com.example.project.dto.PetRequestDto;
import com.example.project.dto.PetResponseDto;
import com.example.project.entity.Owner;
import com.example.project.entity.Pet;
import com.example.project.exception.BusinessValidationException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final OwnerService ownerService;

    @Transactional
    public PetResponseDto registerPet(PetRequestDto requestDto) {
        if (requestDto.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new BusinessValidationException("Pet date of birth cannot be in the future.");
        }

        Owner owner = ownerService.getOwnerEntity(requestDto.getOwnerId());

        Pet pet = Pet.builder()
                .name(requestDto.getName().trim())
                .species(requestDto.getSpecies().trim())
                .breed(requestDto.getBreed().trim())
                .dateOfBirth(requestDto.getDateOfBirth())
                .gender(requestDto.getGender() != null ? requestDto.getGender().trim() : null)
                .weight(requestDto.getWeight())
                .owner(owner)
                .build();

        Pet saved = petRepository.save(pet);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getAllPets(String species, String search, Long ownerId) {
        List<Pet> pets;

        if (ownerId != null) {
            pets = petRepository.findByOwnerId(ownerId);
        } else if (search != null && !search.trim().isEmpty()) {
            pets = petRepository.searchByNameOrBreed(search.trim());
        } else if (species != null && !species.trim().isEmpty() && !species.equalsIgnoreCase("all")) {
            pets = petRepository.findBySpeciesIgnoreCase(species.trim());
        } else {
            pets = petRepository.findAll();
        }

        return pets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PetResponseDto getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + id));
        return mapToResponse(pet);
    }

    public Pet getPetEntity(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + id));
    }

    @Transactional
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pet not found with id: " + id);
        }
        petRepository.deleteById(id);
    }

    public PetResponseDto mapToResponse(Pet pet) {
        Period period = Period.between(pet.getDateOfBirth(), LocalDate.now());
        return PetResponseDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .dateOfBirth(pet.getDateOfBirth())
                .ageYears(period.getYears())
                .ageMonths(period.getMonths())
                .gender(pet.getGender())
                .weight(pet.getWeight())
                .ownerId(pet.getOwner().getId())
                .ownerName(pet.getOwner().getFullName())
                .ownerEmail(pet.getOwner().getEmail())
                .ownerPhone(pet.getOwner().getPhoneNumber())
                .vaccinationRecordCount(pet.getVaccinationRecords() != null ? pet.getVaccinationRecords().size() : 0)
                .createdAt(pet.getCreatedAt())
                .build();
    }
}
