package dev.andre.accidentalert.service;

import dev.andre.accidentalert.entity.*;
import dev.andre.accidentalert.entity.enums.NotificationStatus;
import dev.andre.accidentalert.entity.enums.Role;
import dev.andre.accidentalert.repository.NotificationRepository;
import dev.andre.accidentalert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void notifyManagers(Accident accident) {

        List<User> managers = userRepository.findByRole(Role.ROLE_MANAGER);

        for (User manager : managers) {

            Notification notification = Notification.builder()
                    .message("Novo acidente registrado: " + accident.getDescription())
                    .recipientEmail(manager.getEmail())
                    .status(NotificationStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .accident(accident)
                    .build();

            notificationRepository.save(notification);
        }
    }
}