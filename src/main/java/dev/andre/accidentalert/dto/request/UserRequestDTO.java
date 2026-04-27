package dev.andre.accidentalert.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(

        @NotBlank
        String name,

        @Email
        String email,

        @NotBlank
        String password

) {}
