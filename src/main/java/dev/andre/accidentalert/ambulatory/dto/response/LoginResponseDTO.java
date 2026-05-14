package dev.andre.accidentalert.ambulatory.dto.response;

public record LoginResponseDTO(
        String token,
        Boolean mustChangePassword
) {}
