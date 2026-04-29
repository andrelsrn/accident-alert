package dev.andre.accidentalert.repository;

import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

}
