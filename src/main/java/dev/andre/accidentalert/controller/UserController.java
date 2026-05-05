package dev.andre.accidentalert.controller;

import dev.andre.accidentalert.dto.request.ChangePasswordDTO;
import dev.andre.accidentalert.dto.request.CreateUserDTO;
import dev.andre.accidentalert.dto.request.UpdateUserRoleDTO;
import dev.andre.accidentalert.dto.response.UserResponseDTO;
import dev.andre.accidentalert.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /**
     * Only ADMIN can create users.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public UserResponseDTO createUser(@RequestBody @Valid CreateUserDTO dto){
        return service.createUser(dto);
    }

    /**
     * STAFF+ can change their own password.
     */
    @PreAuthorize("hasRole('ROLE_STAFF')")
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

    /**
     * Only ADMIN can deactivate users.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id){
        service.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Only MANAGER+ can view users, with optional filter by active status.
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(
            @RequestParam boolean active){
        return ResponseEntity.ok(service.findUsersByActive(active));
    }

    /**
     * Only ADMIN can update user roles.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleDTO dto
    ) {
        service.updateUserRole(id, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Only MANAGER+ can view user by ID.
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(service.findUserById(id));
    }
}
