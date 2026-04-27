package dev.andre.accidentalert.dto.response;

import dev.andre.accidentalert.entity.enums.Role;

public record UserResponseDTO(

        Long id,
        String name,
        String email,
        Role role
) {}
