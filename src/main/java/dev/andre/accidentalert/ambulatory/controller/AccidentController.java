package dev.andre.accidentalert.ambulatory.controller;

import dev.andre.accidentalert.ambulatory.dto.request.AccidentRequestDTO;
import dev.andre.accidentalert.ambulatory.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.ambulatory.entity.enums.Severity;
import dev.andre.accidentalert.ambulatory.service.AccidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accidents")
@RequiredArgsConstructor
@Tag(name = "Accidents", description = "Endpoints for managing factory accidents and severity filters.")
public class AccidentController {

    private final AccidentService service;


    @PreAuthorize("hasRole('STAFF')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new accident report", description = "Allowed roles: STAFF or higher.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires STAFF role")
    })
    public AccidentResponseDTO create(@RequestBody @Valid AccidentRequestDTO dto) {
        return service.create(dto);
    }


    @PreAuthorize("hasRole('STAFF')")
    @GetMapping
    @Operation(summary = "List all accident reports with pagination", description = "Allowed roles: STAFF or higher.")
    public ResponseEntity<Page<AccidentResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<AccidentResponseDTO> accidents = service.findAll(pageable);
        return ResponseEntity.ok(accidents);
    }


    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/severity")
    @Operation(summary = "Filter accidents by severity", description = "Retrieves a filtered list based on the severity level (e.g., LOW, HIGH).")
    public ResponseEntity<List<AccidentResponseDTO>> getBySeverity(
            @RequestParam(required = false) Severity severity) {

        return ResponseEntity.ok(service.findBySeverity(severity));
    }


    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{id}")
    @Operation(summary = "Get accident by ID", description = "Allowed roles: STAFF or higher.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Accident report not found")
    })
    public ResponseEntity<AccidentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
