package dev.andre.accidentalert.ambulatory.service;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.ambulatory.entity.Notification;
import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.ambulatory.entity.enums.NotificationStatus;
import dev.andre.accidentalert.ambulatory.entity.enums.Role;
import dev.andre.accidentalert.ambulatory.repository.NotificationRepository;
import dev.andre.accidentalert.ambulatory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public void notifyManagers(Accident accident) {

        List<User> managers = userRepository.findByRole(Role.MANAGER);

        for (User manager : managers) {

            Notification notification = Notification.builder()
                    .message("Novo acidente registrado: " + accident.getDescription())
                    .recipientEmail(manager.getEmail())
                    .status(NotificationStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .accident(accident)
                    .build();



            try {
                emailService.send(
                        manager.getEmail(),
                        "🚨 Novo Acidente",
                        notification.getMessage()
                );

                notification.setStatus(NotificationStatus.SENT);

            } catch (Exception e) {
                notification.setStatus(NotificationStatus.FAILED);
            }

            notificationRepository.save(notification);
        }
    }
}