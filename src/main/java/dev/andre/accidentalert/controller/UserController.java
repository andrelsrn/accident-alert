package dev.andre.accidentalert.controller;

import dev.andre.accidentalert.dto.request.ChangePasswordDTO;
import dev.andre.accidentalert.dto.request.UserRequestDTO;
import dev.andre.accidentalert.dto.response.UserResponseDTO;
import dev.andre.accidentalert.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserResponseDTO createUser(@RequestBody @Valid UserRequestDTO dto){
        return service.createUser(dto);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordDTO dto
    ) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        service.changePassword(dto, email);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id){
        service.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
}
