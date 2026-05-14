package dev.andre.accidentalert.ambulatory.dto.response;

import dev.andre.accidentalert.ambulatory.entity.enums.AccidentStatus;
import dev.andre.accidentalert.ambulatory.entity.enums.Severity;

public record AccidentSummaryDTO(
        Long id,
        String description,
        String location,
        Severity severity,
        String victimName,
        AccidentStatus status,
        UserSummaryDTO createdBy
) {
}
