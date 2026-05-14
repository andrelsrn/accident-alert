package dev.andre.accidentalert.ambulatory.mapper;

import dev.andre.accidentalert.ambulatory.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.ambulatory.entity.Accident;

public class AccidentMapper {

    public static AccidentResponseDTO toResponseDTO(Accident accident) {
        return new AccidentResponseDTO(
                accident.getId(),
                accident.getDescription(),
                accident.getLocation(),
                accident.getSeverity(),
                accident.getCreatedAt(),
                accident.getCreatedBy().getEmail(),
                accident.getVictimName(),
                accident.getVictimDepartment(),
                accident.getStatus()
        );
    }
}
