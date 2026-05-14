package dev.andre.accidentalert.ambulatory.dto.request;

import dev.andre.accidentalert.ambulatory.entity.enums.AccidentStatus;
import dev.andre.accidentalert.ambulatory.entity.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccidentRequestDTO(

        @NotBlank
        String description,

        @NotBlank
        String location,

        @NotNull
        Severity severity,

        @NotBlank
        String victimName,

        @NotBlank
        String victimDepartment,

        @NotNull
        AccidentStatus status
) {}
