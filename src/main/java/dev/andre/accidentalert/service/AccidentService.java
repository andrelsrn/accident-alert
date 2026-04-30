package dev.andre.accidentalert.service;

import dev.andre.accidentalert.dto.request.AccidentRequestDTO;
import dev.andre.accidentalert.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.entity.Accident;
import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.entity.enums.Severity;
import dev.andre.accidentalert.repository.AccidentRepository;
import dev.andre.accidentalert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccidentService {

    private final AccidentRepository accidentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public AccidentResponseDTO create(AccidentRequestDTO dto){
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Accident accident = Accident.builder()
                .description(dto.description())
                .location(dto.location())
                .severity(dto.severity())
                .createdAt(LocalDateTime.now())
                .victimName(dto.victimName())
                .victimDepartment(dto.victimDepartment())
                .createdBy(user)
                .build();

        Accident saved = accidentRepository.save(accident);

        if (saved.getSeverity() == Severity.HIGH ||
        saved.getSeverity() == Severity.CRITICAL) {
            notificationService.notifyManagers(saved);
        }

        return new AccidentResponseDTO(
                saved.getId(),
                saved.getDescription(),
                saved.getLocation(),
                saved.getSeverity(),
                saved.getCreatedAt(),
                user.getEmail(),
                saved.getVictimName(),
                saved.getVictimDepartment()

        );
    }

    public List<AccidentResponseDTO> findAll() {
        return accidentRepository.findAll()
                .stream()
                .map(accident -> new AccidentResponseDTO(
                        accident.getId(),
                        accident.getDescription(),
                        accident.getLocation(),
                        accident.getSeverity(),
                        accident.getCreatedAt(),
                        accident.getVictimName(),
                        accident.getVictimDepartment(),
                        accident.getCreatedBy().getEmail()
                ))
                .toList();
    }

    public List<AccidentResponseDTO> findBySeverity(Severity severity) {

        List<Accident> accidents;

        if (severity != null) {
            accidents = accidentRepository.findBySeverity(severity);
        } else {
            accidents = accidentRepository.findAll();
        }

        return accidents.stream()
                .map(a -> new AccidentResponseDTO(
                        a.getId(),
                        a.getDescription(),
                        a.getLocation(),
                        a.getSeverity(),
                        a.getCreatedAt(),
                        a.getCreatedBy().getEmail(),
                        a.getVictimName(),
                        a.getVictimDepartment()
                ))
                .toList();
    }

}
