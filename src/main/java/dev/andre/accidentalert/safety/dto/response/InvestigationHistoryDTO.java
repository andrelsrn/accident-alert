package dev.andre.accidentalert.safety.dto.response;

import dev.andre.accidentalert.ambulatory.dto.response.UserSummaryDTO;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;

import java.time.LocalDateTime;

public record InvestigationHistoryDTO(
        Long id,
        InvestigationStatus oldStatus,
        InvestigationStatus newStatus,
        String comment,
        UserSummaryDTO changedBy,
        LocalDateTime createdAt
) {}
