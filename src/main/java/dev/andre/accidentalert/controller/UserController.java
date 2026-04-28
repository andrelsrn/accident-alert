package dev.andre.accidentalert.controller;

import dev.andre.accidentalert.dto.request.UserRequestDTO;
import dev.andre.accidentalert.dto.response.UserResponseDTO;
import dev.andre.accidentalert.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
