package dev.andre.accidentalert.safety.service;

import dev.andre.accidentalert.dto.response.AccidentSummaryDTO;
import dev.andre.accidentalert.dto.response.UserSummaryDTO;
import dev.andre.accidentalert.entity.Accident;
import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.repository.AccidentRepository;
import dev.andre.accidentalert.repository.UserRepository;
import dev.andre.accidentalert.safety.dto.request.InvestigationRequestDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationResponseDTO;
import dev.andre.accidentalert.safety.entity.Investigation;
import dev.andre.accidentalert.safety.repository.InvestigationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class InvestigationService {

    private final InvestigationRepository repository;
    private final UserRepository userRepository;
    private final AccidentRepository accidentRepository;

    public InvestigationResponseDTO create(InvestigationRequestDTO dto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


         User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getActive()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is deactivated");
        }

        Accident accident = accidentRepository.findById(dto.accidentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Accident not found"));
        if (repository.existsByAccident(accident))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Accident is already in use ");

        Investigation investigation = Investigation.builder()
                .accident(accident)
                .assignedTechnician(user)
                .rootCause(dto.rootCause())
                .observation(dto.observation())
                .status(dto.status())
                .build();

        Investigation saved = repository.save(investigation);

        UserSummaryDTO summaryDTO = new UserSummaryDTO(
                user.getId(),
                user.getName()
        );

        AccidentSummaryDTO accidentSummaryDTO = new AccidentSummaryDTO(
                accident.getId(),
                accident.getDescription(),
                accident.getLocation(),
                accident.getSeverity(),
                accident.getVictimName(),
                accident.getStatus(),
                summaryDTO
        );

        return new InvestigationResponseDTO(
                saved.getId(),
                accidentSummaryDTO,
                summaryDTO,
                saved.getRootCause(),
                saved.getObservation(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}
