package dev.andre.accidentalert.ambulatory.controller;

import dev.andre.accidentalert.ambulatory.dto.request.ChangePasswordDTO;
import dev.andre.accidentalert.ambulatory.dto.request.CreateUserDTO;
import dev.andre.accidentalert.ambulatory.dto.request.UpdateUserRoleDTO;
import dev.andre.accidentalert.ambulatory.dto.response.UserResponseDTO;
import dev.andre.accidentalert.ambulatory.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users, including creation, password changes, deactivation, and role updates.")
public class UserController {

    private final UserService service;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user", description = "Only ADMIN can create new users.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires ADMIN role")
    })
    public UserResponseDTO createUser(@RequestBody @Valid CreateUserDTO dto) {
        return service.createUser(dto);
    }


    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("/password")
    @Operation(summary = "Change password", description = "Allows authenticated users to change their own password. Requires current password for verification.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid current password"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires STAFF role")
    })
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordDTO dto
    ) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        service.changePassword(dto, email);

        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a user", description = "Only ADMIN can deactivate users. Deactivated users cannot log in.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deactivated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires ADMIN role")
    })
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        service.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    @Operation(summary = "List users by active status", description = "Only MANAGER+ can view users. Filter by active status (true for active users, false for deactivated users).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires MANAGER role")
    })
    public ResponseEntity<List<UserResponseDTO>> getUsers(
            @RequestParam boolean active) {
        return ResponseEntity.ok(service.findUsersByActive(active));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role", description = "Only ADMIN can update user roles.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User role updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires ADMIN role")
    })
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleDTO dto
    ) {
        service.updateUserRole(id, dto);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Only MANAGER+ can view user details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires MANAGER role"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findUserById(id));
    }
}
