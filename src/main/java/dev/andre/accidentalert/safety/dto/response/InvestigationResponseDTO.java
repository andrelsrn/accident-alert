package dev.andre.accidentalert.safety.dto.response;

import dev.andre.accidentalert.ambulatory.dto.response.AccidentSummaryDTO;
import dev.andre.accidentalert.ambulatory.dto.response.UserSummaryDTO;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;

import java.time.LocalDateTime;

public record InvestigationResponseDTO(
        Long id,
        AccidentSummaryDTO accident,
        UserSummaryDTO assignedTechnician,
        String rootCause,
        String observation,
        InvestigationStatus status,
        LocalDateTime createdAt
)
{}
