package dev.andre.accidentalert.safety.mapper;

import dev.andre.accidentalert.ambulatory.dto.response.UserSummaryDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationHistoryDTO;
import dev.andre.accidentalert.safety.entity.InvestigationHistory;

public class InvestigationHistoryMapper {

    public static InvestigationHistoryDTO toResponseDTO(
            InvestigationHistory history
    ) {

        UserSummaryDTO changedBy = new UserSummaryDTO(
                history.getChangedBy().getId(),
                history.getChangedBy().getName()
        );

        return new InvestigationHistoryDTO(
                history.getId(),
                history.getOldStatus(),
                history.getNewStatus(),
                history.getComment(),
                changedBy,
                history.getCreatedAt()
        );
    }
}