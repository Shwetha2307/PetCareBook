package com.example.project.controller;

import com.example.project.dto.VaccineTypeRequestDto;
import com.example.project.dto.VaccineTypeResponseDto;
import com.example.project.service.VaccineTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccine-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VaccineTypeController {

    private final VaccineTypeService vaccineTypeService;

    @PostMapping
    public ResponseEntity<VaccineTypeResponseDto> createVaccineType(@Valid @RequestBody VaccineTypeRequestDto requestDto) {
        VaccineTypeResponseDto response = vaccineTypeService.createVaccineType(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VaccineTypeResponseDto>> getAllVaccineTypes(
            @RequestParam(required = false) String species) {
        return ResponseEntity.ok(vaccineTypeService.getAllVaccineTypes(species));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccineTypeResponseDto> getVaccineTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(vaccineTypeService.getVaccineTypeById(id));
    }
}
