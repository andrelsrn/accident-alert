package dev.andre.accidentalert.safety.controller;

import dev.andre.accidentalert.safety.dto.request.InvestigationRequestDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationHistoryDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationResponseDTO;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import dev.andre.accidentalert.safety.service.InvestigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/safety/investigation")
@RequiredArgsConstructor
@Tag(name = "Investigation", description = "Endpoints for managing investigations related to accidents.")
public class InvestigationController {

    private final InvestigationService service;

    @PreAuthorize("hasRole('SAFETY_TECHNICIAN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new investigation", description = "Registers a new accident investigation.")
    @ApiResponse(responseCode = "201", description = "Created")
    public InvestigationResponseDTO create(@RequestBody @Valid InvestigationRequestDTO dto) {
        return service.create(dto);
    }

    @PreAuthorize("hasRole('SAFETY_TECHNICIAN')")
    @GetMapping
    @Operation(summary = "List all investigations with pagination", description = "Only Safety Technician can access this endpoint.")
    public ResponseEntity<Page<InvestigationResponseDTO>> getAllInvestigations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.ofSize(size).withPage(page);
        Page<InvestigationResponseDTO> investigations = service.findAll(pageable);
        return ResponseEntity.ok(investigations);
    }

    @PreAuthorize("hasRole('SAFETY_TECHNICIAN')")
    @GetMapping("/{id}")
    @Operation(summary = "Find investigation by ID", description = "Only Safety Technician can access this endpoint.")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public InvestigationResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PreAuthorize("hasRole('SAFETY_TECHNICIAN')")
    @GetMapping("/status/{status}")
    @Operation(summary = "Find investigations by status", description = "Only Safety Technician can access this endpoint.")
    public List<InvestigationResponseDTO> findByStatus(@PathVariable InvestigationStatus status) {
        return service.findByStatus(status);
    }

    @PreAuthorize("hasRole('SAFETY_TECHNICIAN')")
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update investigation status", description = "Only Safety Technician can access this endpoint.")
    public ResponseEntity<InvestigationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam InvestigationStatus status,
            @RequestParam String observation) {
        InvestigationResponseDTO response = service.updateStatus(id, status, observation);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SAFETY_TECHNICIAN')")
    @GetMapping("/{id}/history")
    @Operation(
            summary = "Get investigation history",
            description = "Returns the complete investigation status timeline."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Investigation not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<InvestigationHistoryDTO>> getHistory(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(service.getHistory(id));
    }


}
