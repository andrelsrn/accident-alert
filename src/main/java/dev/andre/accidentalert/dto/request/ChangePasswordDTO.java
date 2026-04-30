package dev.andre.accidentalert.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
        @NotBlank
        String currentPassword,

        @NotBlank
        @Size(min = 6, message = "New password must be at least 6 characters long")
        String newPassword
) {}
