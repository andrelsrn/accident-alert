package dev.andre.accidentalert.ambulatory.dto.request;

import dev.andre.accidentalert.ambulatory.entity.enums.Role;

public record UpdateUserRoleDTO(
        Role role
) {
}
