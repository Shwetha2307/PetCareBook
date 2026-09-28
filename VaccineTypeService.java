package com.example.project.service;

import com.example.project.dto.VaccineTypeRequestDto;
import com.example.project.dto.VaccineTypeResponseDto;
import com.example.project.entity.VaccineType;
import com.example.project.exception.BusinessValidationException;
import com.example.project.exception.DuplicateResourceException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.VaccineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccineTypeService {

    private final VaccineTypeRepository vaccineTypeRepository;

    @Transactional
    public VaccineTypeResponseDto createVaccineType(VaccineTypeRequestDto requestDto) {
        if (vaccineTypeRepository.existsByNameIgnoreCase(requestDto.getName().trim())) {
            throw new DuplicateResourceException("Vaccine type with name '" + requestDto.getName() + "' already exists.");
        }

        if (requestDto.getStandardIntervalDays() == null || requestDto.getStandardIntervalDays() <= 0) {
            throw new BusinessValidationException("Standard interval must be greater than 0 days.");
        }

        VaccineType vaccineType = VaccineType.builder()
                .name(requestDto.getName().trim())
                .targetSpecies(requestDto.getTargetSpecies().trim())
                .description(requestDto.getDescription() != null ? requestDto.getDescription().trim() : null)
                .standardIntervalDays(requestDto.getStandardIntervalDays())
                .mandatory(requestDto.getMandatory() != null ? requestDto.getMandatory() : true)
                .build();

        VaccineType saved = vaccineTypeRepository.save(vaccineType);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<VaccineTypeResponseDto> getAllVaccineTypes(String species) {
        List<VaccineType> list;
        if (species != null && !species.trim().isEmpty() && !species.equalsIgnoreCase("all")) {
            list = vaccineTypeRepository.findByTargetSpeciesIgnoreCaseOrTargetSpeciesIgnoreCase(species.trim(), "all");
        } else {
            list = vaccineTypeRepository.findAll();
        }

        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VaccineTypeResponseDto getVaccineTypeById(Long id) {
        VaccineType vaccineType = vaccineTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine type not found with id: " + id));
        return mapToResponse(vaccineType);
    }

    public VaccineType getVaccineTypeEntity(Long id) {
        return vaccineTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine type not found with id: " + id));
    }

    public VaccineType getVaccineTypeByName(String name) {
        return vaccineTypeRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine type not found with name: " + name));
    }

    public VaccineTypeResponseDto mapToResponse(VaccineType vt) {
        return VaccineTypeResponseDto.builder()
                .id(vt.getId())
                .name(vt.getName())
                .targetSpecies(vt.getTargetSpecies())
                .description(vt.getDescription())
                .standardIntervalDays(vt.getStandardIntervalDays())
                .mandatory(vt.getMandatory())
                .createdAt(vt.getCreatedAt())
                .build();
    }
}
