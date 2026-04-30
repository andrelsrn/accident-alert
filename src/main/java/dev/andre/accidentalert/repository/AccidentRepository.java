package dev.andre.accidentalert.repository;

import dev.andre.accidentalert.entity.Accident;
import dev.andre.accidentalert.entity.enums.Severity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Long> {


    List<Accident> findBySeverity(Severity severity);
}