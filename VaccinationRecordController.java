package com.example.project.controller;

import com.example.project.dto.VaccinationRecordRequestDto;
import com.example.project.dto.VaccinationRecordResponseDto;
import com.example.project.service.VaccinationRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccinations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VaccinationRecordController {

    private final VaccinationRecordService vaccinationRecordService;

    @PostMapping
    public ResponseEntity<VaccinationRecordResponseDto> logVaccination(
            @Valid @RequestBody VaccinationRecordRequestDto requestDto) {
        VaccinationRecordResponseDto response = vaccinationRecordService.logVaccination(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VaccinationRecordResponseDto>> getAllVaccinations() {
        return ResponseEntity.ok(vaccinationRecordService.getAllVaccinations());
    }

    @GetMapping("/due-soon")
    public ResponseEntity<List<VaccinationRecordResponseDto>> getVaccinationsDueSoon(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(vaccinationRecordService.getVaccinationsDueInDays(days));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<VaccinationRecordResponseDto>> getOverdueVaccinations() {
        return ResponseEntity.ok(vaccinationRecordService.getOverdueVaccinations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccinationRecordResponseDto> getVaccinationById(@PathVariable Long id) {
        return ResponseEntity.ok(vaccinationRecordService.getVaccinationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable Long id) {
        vaccinationRecordService.deleteVaccination(id);
        return ResponseEntity.noContent().build();
    }
}
