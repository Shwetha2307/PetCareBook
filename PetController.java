package com.example.project.controller;

import com.example.project.dto.PetRequestDto;
import com.example.project.dto.PetResponseDto;
import com.example.project.dto.VaccinationRecordResponseDto;
import com.example.project.service.PetService;
import com.example.project.service.VaccinationRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;
    private final VaccinationRecordService vaccinationRecordService;

    @PostMapping
    public ResponseEntity<PetResponseDto> registerPet(@Valid @RequestBody PetRequestDto requestDto) {
        PetResponseDto response = petService.registerPet(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getAllPets(
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long ownerId) {
        return ResponseEntity.ok(petService.getAllPets(species, search, ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDto> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @GetMapping("/{id}/vaccinations")
    public ResponseEntity<List<VaccinationRecordResponseDto>> getPetVaccinationHistory(@PathVariable Long id) {
        return ResponseEntity.ok(vaccinationRecordService.getPetVaccinationHistory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
