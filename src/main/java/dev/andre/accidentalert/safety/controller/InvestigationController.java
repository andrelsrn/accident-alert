package dev.andre.accidentalert.safety.controller;

import dev.andre.accidentalert.safety.dto.request.InvestigationRequestDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationResponseDTO;
import dev.andre.accidentalert.safety.entity.Investigation;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import dev.andre.accidentalert.safety.service.InvestigationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/investigation/{id}/status")
    public ResponseEntity<InvestigationResponseDTO> updateStatus(@PathVariable Long id, @RequestParam InvestigationStatus status,@RequestParam String observation) {
        InvestigationResponseDTO response = service.updateStatus(id, status,observation);
        return ResponseEntity.ok(response);
    }
}
