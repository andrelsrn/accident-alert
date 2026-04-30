package dev.andre.accidentalert.controller;

import dev.andre.accidentalert.dto.request.AccidentRequestDTO;
import dev.andre.accidentalert.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.entity.enums.Severity;
import dev.andre.accidentalert.service.AccidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accidents")
@RequiredArgsConstructor
public class AccidentController {

    private final AccidentService service;


    @PostMapping
    public AccidentResponseDTO create(@RequestBody @Valid AccidentRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<AccidentResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/severity")
    public ResponseEntity<List<AccidentResponseDTO>> getBySeverity(
            @RequestParam(required = false) Severity severity) {

        return ResponseEntity.ok(service.findBySeverity(severity));
    }
}
