package com.example.project.service;

import com.example.project.dto.OwnerRequestDto;
import com.example.project.dto.OwnerResponseDto;
import com.example.project.entity.Owner;
import com.example.project.exception.DuplicateResourceException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional
    public OwnerResponseDto registerOwner(OwnerRequestDto requestDto) {
        if (ownerRepository.existsByEmail(requestDto.getEmail().trim())) {
            throw new DuplicateResourceException("An owner with email '" + requestDto.getEmail() + "' already exists.");
        }

        Owner owner = Owner.builder()
                .fullName(requestDto.getFullName().trim())
                .email(requestDto.getEmail().trim().toLowerCase())
                .phoneNumber(requestDto.getPhoneNumber().trim())
                .address(requestDto.getAddress() != null ? requestDto.getAddress().trim() : null)
                .build();

        Owner saved = ownerRepository.save(owner);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OwnerResponseDto> getAllOwners() {
        return ownerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OwnerResponseDto getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + id));
        return mapToResponse(owner);
    }

    @Transactional
    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
    }

    public Owner getOwnerEntity(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + id));
    }

    public OwnerResponseDto mapToResponse(Owner owner) {
        return OwnerResponseDto.builder()
                .id(owner.getId())
                .fullName(owner.getFullName())
                .email(owner.getEmail())
                .phoneNumber(owner.getPhoneNumber())
                .address(owner.getAddress())
                .petCount(owner.getPets() != null ? owner.getPets().size() : 0)
                .createdAt(owner.getCreatedAt())
                .build();
    }
}
