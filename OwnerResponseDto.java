package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private int petCount;
    private LocalDateTime createdAt;
}
