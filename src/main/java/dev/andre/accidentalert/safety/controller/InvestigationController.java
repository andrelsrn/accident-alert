package dev.andre.accidentalert.safety.controller;

import dev.andre.accidentalert.safety.dto.request.InvestigationRequestDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationResponseDTO;
import dev.andre.accidentalert.safety.entity.Investigation;
import dev.andre.accidentalert.safety.service.InvestigationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/safety")
@RequiredArgsConstructor
public class InvestigationController {

    private final InvestigationService service;


    @PostMapping("/investigation")
    public InvestigationResponseDTO create(@RequestBody @Valid InvestigationRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/investigation")
    public List<InvestigationResponseDTO> findAll() {
        return service.findAll();
    }
}
