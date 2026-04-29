package dev.andre.accidentalert.repository;

import dev.andre.accidentalert.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {


}
