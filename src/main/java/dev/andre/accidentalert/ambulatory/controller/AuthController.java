package dev.andre.accidentalert.ambulatory.controller;

import dev.andre.accidentalert.ambulatory.dto.request.LoginRequestDTO;
import dev.andre.accidentalert.ambulatory.dto.response.LoginResponseDTO;
import dev.andre.accidentalert.ambulatory.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and token management.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Authenticate user and generate JWT token",
            description = "Allows users to log in with their credentials and receive a" +
                    " JWT token for authenticated access to protected endpoints.")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }

}
