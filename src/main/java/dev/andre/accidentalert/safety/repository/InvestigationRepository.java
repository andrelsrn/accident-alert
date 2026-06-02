package dev.andre.accidentalert.safety.repository;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.safety.entity.Investigation;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestigationRepository extends JpaRepository<Investigation, Long> {
    boolean existsByAccident(Accident accident);

    Page<Investigation> findInvestigationByStatus(InvestigationStatus status, Pageable pageable);
}
