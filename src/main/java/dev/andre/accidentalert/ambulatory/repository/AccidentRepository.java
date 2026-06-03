package dev.andre.accidentalert.ambulatory.repository;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.ambulatory.entity.enums.Severity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccidentRepository extends JpaRepository<Accident, Long> {


    Page<Accident> findBySeverity(Severity severity, Pageable pageable);

    long countBySeverity(Severity severity);
}