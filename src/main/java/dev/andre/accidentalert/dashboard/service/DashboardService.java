package dev.andre.accidentalert.dashboard.service;

import dev.andre.accidentalert.ambulatory.entity.enums.Severity;
import dev.andre.accidentalert.ambulatory.repository.AccidentRepository;
import dev.andre.accidentalert.dashboard.dto.DashboardSummaryDTO;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import dev.andre.accidentalert.safety.repository.InvestigationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AccidentRepository accidentRepository;
    private final InvestigationRepository investigationRepository;

    public DashboardSummaryDTO getSummary() {

        long totalAccidents = accidentRepository.count();

        long highSeverityAccidents = accidentRepository.countBySeverity(Severity.HIGH);

        long openInvestigations = investigationRepository.countByStatus(InvestigationStatus.OPEN);

        long closedInvestigations = investigationRepository.countByStatus(InvestigationStatus.CLOSED);

    return new DashboardSummaryDTO(
            totalAccidents,
            highSeverityAccidents,
            openInvestigations,
            closedInvestigations
    );
    }

}
