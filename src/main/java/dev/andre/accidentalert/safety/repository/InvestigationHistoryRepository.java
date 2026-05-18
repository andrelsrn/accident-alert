package dev.andre.accidentalert.safety.repository;

import dev.andre.accidentalert.safety.entity.InvestigationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestigationHistoryRepository extends JpaRepository<InvestigationHistory, Long> {
}
