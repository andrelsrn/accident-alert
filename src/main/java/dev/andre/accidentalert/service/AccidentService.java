package dev.andre.accidentalert.service;

import dev.andre.accidentalert.dto.request.AccidentRequestDTO;
import dev.andre.accidentalert.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.entity.Accident;
import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.repository.AccidentRepository;
import dev.andre.accidentalert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccidentService {

    private final AccidentRepository accidentRepository;
    private final UserRepository userRepository;

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
                .createdBy(user)
                .build();


        Accident saved = accidentRepository.save(accident);

        return new AccidentResponseDTO(
                saved.getId(),
                saved.getDescription(),
                saved.getLocation(),
                saved.getSeverity(),
                saved.getCreatedAt(),
                user.getEmail()
        );
    }
}
