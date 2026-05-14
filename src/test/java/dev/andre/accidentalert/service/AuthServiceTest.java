package dev.andre.accidentalert.service;

import dev.andre.accidentalert.ambulatory.dto.request.LoginRequestDTO;
import dev.andre.accidentalert.ambulatory.dto.response.LoginResponseDTO;
import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.ambulatory.entity.enums.Role;
import dev.andre.accidentalert.ambulatory.repository.UserRepository;
import dev.andre.accidentalert.ambulatory.service.AuthService;
import dev.andre.accidentalert.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("user@email.com");
        user.setPassword("encoded-password");
        user.setActive(true);
        user.setMustChangePassword(false);
        user.setRole(Role.STAFF);
    }

    // Successful login
    @Test
    void shouldLoginSuccessfully() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);

        when(jwtService.generateToken(any()))
                .thenReturn("fake-jwt-token");

        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "123");

        LoginResponseDTO response = authService.login(dto);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
    }

    // User not found
    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        LoginRequestDTO dto = new LoginRequestDTO("x@email.com", "123");

        assertThrows(ResponseStatusException.class, () -> {
            authService.login(dto);
        });
    }

    // User is inactive
    @Test
    void shouldThrowWhenUserInactive() {
        user.setActive(false);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "123");

        assertThrows(ResponseStatusException.class, () -> {
            authService.login(dto);
        });
    }

    // Password is incorrect
    @Test
    void shouldThrowWhenPasswordIncorrect() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "wrong");

        assertThrows(ResponseStatusException.class, () -> {
            authService.login(dto);
        });
    }

    // User needs to change password but should still be able to login
    @Test
    void shouldAllowLoginEvenWhenMustChangePassword() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("encoded");
        user.setActive(true);
        user.setMustChangePassword(true);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);

        when(jwtService.generateToken(any()))
                .thenReturn("token123");

        LoginRequestDTO dto = new LoginRequestDTO("test@email.com", "123");

        LoginResponseDTO response = authService.login(dto);

        assertNotNull(response);
        assertNotNull(response.token());
    }
}
