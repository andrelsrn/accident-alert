package dev.andre.accidentalert.ambulatory.repository;

import dev.andre.accidentalert.ambulatory.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {


}
