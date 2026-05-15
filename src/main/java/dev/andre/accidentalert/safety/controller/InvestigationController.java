package dev.andre.accidentalert.safety.controller;

import dev.andre.accidentalert.safety.dto.request.InvestigationRequestDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationResponseDTO;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import dev.andre.accidentalert.safety.service.InvestigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/safety/investigation")
@RequiredArgsConstructor
@Tag(name = "Investigation", description = "Endpoints for managing investigations related to accidents.")
public class InvestigationController {

    private final InvestigationService service;


    @PostMapping // URL final: /safety/investigation
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new investigation", description = "Registers a new accident investigation.")
    @ApiResponse(responseCode = "201", description = "Created")
    public InvestigationResponseDTO create(@RequestBody @Valid InvestigationRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping // URL final: /safety/investigation
    @Operation(summary = "List all investigations")
    public List<InvestigationResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}") // URL final: /safety/investigation/{id}
    @Operation(summary = "Find investigation by ID")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public InvestigationResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/status/{status}") // URL final: /safety/investigation/status/{status}
    @Operation(summary = "Find investigations by status")
    public List<InvestigationResponseDTO> findByStatus(@PathVariable InvestigationStatus status) {
        return service.findByStatus(status);
    }

    @PatchMapping("/{id}/status") // URL final: /safety/investigation/{id}/status
    @Operation(summary = "Update investigation status")
    public ResponseEntity<InvestigationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam InvestigationStatus status,
            @RequestParam String observation) {
        InvestigationResponseDTO response = service.updateStatus(id, status, observation);
        return ResponseEntity.ok(response);
    }
}
