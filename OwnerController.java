package com.example.project.controller;

import com.example.project.dto.OwnerRequestDto;
import com.example.project.dto.OwnerResponseDto;
import com.example.project.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<OwnerResponseDto> registerOwner(@Valid @RequestBody OwnerRequestDto requestDto) {
        OwnerResponseDto response = ownerService.registerOwner(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OwnerResponseDto>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> getOwnerById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }
}
