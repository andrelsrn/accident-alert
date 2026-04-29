package dev.andre.accidentalert.repository;

import dev.andre.accidentalert.entity.Accident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccidentRepository extends JpaRepository<Accident, Long> {

}
