package dev.andre.accidentalert.ambulatory.dto.request;

import dev.andre.accidentalert.ambulatory.entity.enums.Role;

public record CreateUserDTO(
        String name,
        String email,
        Role role
) {}
