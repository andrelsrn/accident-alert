package dev.andre.accidentalert.ambulatory.repository;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.ambulatory.entity.enums.Severity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Long> {


    List<Accident> findBySeverity(Severity severity);

    Long id(Long id);
}