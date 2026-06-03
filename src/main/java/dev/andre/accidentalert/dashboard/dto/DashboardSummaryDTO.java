package dev.andre.accidentalert.dashboard.dto;

public record DashboardSummaryDTO(

        long totalAccidents,
        long highSeverityAccidents,

        long openInvestigations,
        long closedInvestigations
) {
}
