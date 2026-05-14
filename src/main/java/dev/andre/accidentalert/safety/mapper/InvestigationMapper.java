package dev.andre.accidentalert.safety.mapper;

import dev.andre.accidentalert.ambulatory.dto.response.AccidentSummaryDTO;
import dev.andre.accidentalert.ambulatory.dto.response.UserSummaryDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationResponseDTO;
import dev.andre.accidentalert.safety.entity.Investigation;

public class InvestigationMapper {

    public static InvestigationResponseDTO toResponseDTO(Investigation investigation) {
        UserSummaryDTO technicianDTO = new UserSummaryDTO(
                investigation.getAssignedTechnician().getId(),
                investigation.getAssignedTechnician().getName()
        );

        UserSummaryDTO createdByDTO = new UserSummaryDTO(
                investigation.getAccident().getCreatedBy().getId(),
                investigation.getAccident().getCreatedBy().getName()
        );

        AccidentSummaryDTO accidentDTO = new AccidentSummaryDTO(
                investigation.getAccident().getId(),
                investigation.getAccident().getDescription(),
                investigation.getAccident().getLocation(),
                investigation.getAccident().getSeverity(),
                investigation.getAccident().getVictimName(),
                investigation.getAccident().getStatus(),
                createdByDTO
        );

        return new InvestigationResponseDTO(
                investigation.getId(),
                accidentDTO,
                technicianDTO,
                investigation.getRootCause(),
                investigation.getObservation(),
                investigation.getStatus(),
                investigation.getCreatedAt()
        );
    }
}