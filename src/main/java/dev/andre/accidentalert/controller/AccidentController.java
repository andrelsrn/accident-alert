package dev.andre.accidentalert.controller;

import dev.andre.accidentalert.dto.request.AccidentRequestDTO;
import dev.andre.accidentalert.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.entity.enums.Severity;
import dev.andre.accidentalert.service.AccidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accidents")
@RequiredArgsConstructor
public class AccidentController {

    private final AccidentService service;

    /**
     * STAFF+ can create accidents
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping
    public AccidentResponseDTO create(@RequestBody @Valid AccidentRequestDTO dto) {
        return service.create(dto);
    }

    /**
     * STAFF+ can view accidents
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping
    public List<AccidentResponseDTO> findAll() {
        return service.findAll();
    }

    /**
     * Filter by severity
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/severity")
    public ResponseEntity<List<AccidentResponseDTO>> getBySeverity(
            @RequestParam(required = false) Severity severity) {

        return ResponseEntity.ok(service.findBySeverity(severity));
    }

    /**
     * STAFF+ can view accident by ID
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<AccidentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
