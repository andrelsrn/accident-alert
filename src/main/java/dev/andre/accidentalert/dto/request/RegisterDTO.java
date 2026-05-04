package dev.andre.accidentalert.dto.request;

public record RegisterDTO(
        String name,
        String email,
        String password
) {
}
