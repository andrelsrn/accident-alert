package dev.andre.accidentalert.safety.repository;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.safety.entity.Investigation;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestigationRepository extends JpaRepository<Investigation, Long> {
    boolean existsByAccident(Accident accident);

    List<Investigation> findInvestigationByStatus(InvestigationStatus status);
}
