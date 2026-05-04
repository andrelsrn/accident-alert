package dev.andre.accidentalert.dto.request;

import dev.andre.accidentalert.entity.enums.Role;

public record UpdateUserRoleDTO(
        Role role
) {
}
