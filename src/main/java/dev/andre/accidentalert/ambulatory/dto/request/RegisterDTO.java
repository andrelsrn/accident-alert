package dev.andre.accidentalert.ambulatory.dto.request;

public record RegisterDTO(
        String name,
        String email,
        String password
) {
}
