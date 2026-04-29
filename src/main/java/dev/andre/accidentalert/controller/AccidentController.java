package dev.andre.accidentalert.controller;

import dev.andre.accidentalert.dto.request.AccidentRequestDTO;
import dev.andre.accidentalert.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.service.AccidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/accidents")
@RequiredArgsConstructor
public class AccidentController {

    private final AccidentService service;

    @PostMapping
    public AccidentResponseDTO create(@RequestBody @Valid AccidentRequestDTO dto) {
        return service.create(dto);
    }
}
