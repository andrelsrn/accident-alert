package dev.andre.accidentalert.controller;

import dev.andre.accidentalert.dto.request.UserRequestDTO;
import dev.andre.accidentalert.dto.response.UserResponseDTO;
import dev.andre.accidentalert.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserResponseDTO createUser(@RequestBody @Valid UserRequestDTO dto){
        System.out.println("Cheegou no endpoint");
        return service.createUser(dto);
    }
}
