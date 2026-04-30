package dev.andre.accidentalert.dto.response;


import dev.andre.accidentalert.entity.enums.Severity;

import java.time.LocalDateTime;

public record AccidentResponseDTO(

        Long id,
        String description,
        String location,
        Severity severity,
        LocalDateTime createdAt,
        String createdBy,
        String victimName,
        String victimDepartment
) {}