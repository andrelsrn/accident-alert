package dev.andre.accidentalert.dashboard.controller;

import dev.andre.accidentalert.dashboard.dto.DashboardSummaryDTO;
import dev.andre.accidentalert.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints for retrieving dashboard summary statistics.")
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/summary")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get dashboard summary", description = "Returns aggregated statistics for the dashboard overview.")
    public ResponseEntity<DashboardSummaryDTO> summary() {

        return ResponseEntity.ok(
                service.getSummary()
        );
    }
}
