package dev.andre.accidentalert.service;

import dev.andre.accidentalert.dto.request.LoginRequestDTO;
import dev.andre.accidentalert.dto.response.LoginResponseDTO;
import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user =  repository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        boolean passwordMatches = passwordEncoder.matches(
                dto.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Password is incorrect");
        }

        return new LoginResponseDTO("Login successful");
    }
}
