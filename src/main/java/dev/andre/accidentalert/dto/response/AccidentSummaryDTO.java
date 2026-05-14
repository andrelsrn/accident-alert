package dev.andre.accidentalert.dto.response;

import dev.andre.accidentalert.entity.enums.AccidentStatus;
import dev.andre.accidentalert.entity.enums.Severity;

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
