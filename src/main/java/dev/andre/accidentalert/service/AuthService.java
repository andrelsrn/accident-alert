package dev.andre.accidentalert.service;

import dev.andre.accidentalert.dto.request.LoginRequestDTO;
import dev.andre.accidentalert.dto.request.RegisterDTO;
import dev.andre.accidentalert.dto.response.LoginResponseDTO;
import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.repository.UserRepository;
import dev.andre.accidentalert.security.JwtService;
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
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user =  repository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getActive()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "User account is deactivated");
        }

        boolean passwordMatches = passwordEncoder.matches(
                dto.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Password is incorrect");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDTO(token);
    }

    public LoginResponseDTO register(RegisterDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(dev.andre.accidentalert.entity.enums.Role.ROLE_STAFF)
                .active(true)
                .build();

        repository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDTO(token);
    }
}
