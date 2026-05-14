package dev.andre.accidentalert.ambulatory.repository;

import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.ambulatory.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByActive(boolean active);

    String email(String email);
}
