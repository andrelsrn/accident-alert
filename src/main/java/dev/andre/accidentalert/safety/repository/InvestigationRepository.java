package dev.andre.accidentalert.safety.repository;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.safety.entity.Investigation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestigationRepository extends JpaRepository<Investigation, Long> {
    boolean existsByAccident(Accident accident);
}
