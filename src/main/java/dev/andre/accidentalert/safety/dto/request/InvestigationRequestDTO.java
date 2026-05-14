package dev.andre.accidentalert.safety.dto.request;

import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InvestigationRequestDTO(

        @NotNull
        Long accidentId,

        String rootCause,

        @NotBlank
        String observation,

        @NotNull
        InvestigationStatus status

) {
}
