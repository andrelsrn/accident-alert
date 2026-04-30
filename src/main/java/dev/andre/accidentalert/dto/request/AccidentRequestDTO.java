package dev.andre.accidentalert.dto.request;

import dev.andre.accidentalert.entity.enums.Severity;
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
        String victimDepartment
) {}
