package dev.andre.accidentalert.ambulatory.dto.response;


import dev.andre.accidentalert.ambulatory.entity.enums.AccidentStatus;
import dev.andre.accidentalert.ambulatory.entity.enums.Severity;

import java.time.LocalDateTime;


public record AccidentResponseDTO(

        Long id,
        String description,
        String location,
        Severity severity,
        LocalDateTime createdAt,
        String createdBy,
        String victimName,
        String victimDepartment,
        AccidentStatus status
) {}