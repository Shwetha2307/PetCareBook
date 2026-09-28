package com.example.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRequestDto {

    @NotBlank(message = "Owner full name is required")
    private String fullName;

    @NotBlank(message = "Owner email is required")
    @Email(message = "Owner email must be a valid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;
}
