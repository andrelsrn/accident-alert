package dev.andre.accidentalert.ambulatory.service;

import dev.andre.accidentalert.ambulatory.dto.request.AccidentRequestDTO;
import dev.andre.accidentalert.ambulatory.dto.response.AccidentResponseDTO;
import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.ambulatory.entity.enums.Severity;
import dev.andre.accidentalert.ambulatory.mapper.AccidentMapper;
import dev.andre.accidentalert.ambulatory.repository.AccidentRepository;
import dev.andre.accidentalert.ambulatory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccidentService {

    private final AccidentRepository accidentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public AccidentResponseDTO create(AccidentRequestDTO dto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // username aqui é o email do usuário.
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getActive()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is deactivated");
        }

        Accident accident = Accident.builder()
                .description(dto.description())
                .location(dto.location())
                .severity(dto.severity())
                .createdAt(LocalDateTime.now())
                .victimName(dto.victimName())
                .victimDepartment(dto.victimDepartment())
                .createdBy(user)
                .status(dto.status())
                .build();

        Accident saved = accidentRepository.save(accident);

        if (saved.getSeverity() == Severity.HIGH ||
        saved.getSeverity() == Severity.CRITICAL) {
            notificationService.notifyManagers(saved);
        }

        return AccidentMapper.toResponseDTO(saved);
    }

    public Page<AccidentResponseDTO> findAll(Severity severity, Pageable pageable) {
        Page<Accident> accidents;

        if (severity != null) {
            accidents = accidentRepository.findBySeverity(severity, pageable);
        } else {
            accidents = accidentRepository.findAll(pageable);
        }

        return accidents.map(AccidentMapper::toResponseDTO);

    }

    public AccidentResponseDTO findById(Long id) {
        Accident accident = accidentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Accident not found"));

        return AccidentMapper.toResponseDTO(accident);
    }
}
