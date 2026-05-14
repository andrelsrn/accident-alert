package dev.andre.accidentalert.ambulatory.dto.response;

import dev.andre.accidentalert.ambulatory.entity.enums.Role;

public record UserResponseDTO(

        Long id,
        String name,
        String email,
        Role role,
        Boolean active
) {}
