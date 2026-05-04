package dev.andre.accidentalert.dto.request;

import dev.andre.accidentalert.entity.enums.Role;

public record CreateUserDTO(
        String name,
        String email,
        Role role
) {}
