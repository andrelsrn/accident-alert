package dev.andre.accidentalert.service;

import dev.andre.accidentalert.ambulatory.dto.request.LoginRequestDTO;
import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.ambulatory.repository.UserRepository;
import dev.andre.accidentalert.ambulatory.service.AuthService;
import dev.andre.accidentalert.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;


    @Test
    void shouldThrowWhenPasswordIncorrect() {
        User user = new User();
        user.setPassword("encoded");
        user.setActive(true);

        when(repository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(encoder.matches(any(), any()))
                .thenReturn(false);

        LoginRequestDTO dto = new LoginRequestDTO("a", "b");

        assertThrows(ResponseStatusException.class, () -> {
            authService.login(dto);
        });
    }

    @Test
    void shouldThrowWhenUserInactive() {
        User user = new User();
        user.setActive(false);

        when(repository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        LoginRequestDTO dto = new LoginRequestDTO("a", "b");

        assertThrows(ResponseStatusException.class, () -> {
            authService.login(dto);
        });
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(repository.findByEmail(any()))
                .thenReturn(Optional.empty());

        LoginRequestDTO dto = new LoginRequestDTO("x@email.com", "123");

        assertThrows(ResponseStatusException.class, () -> {
            authService.login(dto);
        });
    }

}
