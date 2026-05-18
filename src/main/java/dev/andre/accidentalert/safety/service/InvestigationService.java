package dev.andre.accidentalert.safety.service;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.ambulatory.entity.enums.Role;
import dev.andre.accidentalert.ambulatory.repository.AccidentRepository;
import dev.andre.accidentalert.ambulatory.repository.UserRepository;
import dev.andre.accidentalert.safety.dto.request.InvestigationRequestDTO;
import dev.andre.accidentalert.safety.dto.response.InvestigationResponseDTO;
import dev.andre.accidentalert.safety.entity.Investigation;
import dev.andre.accidentalert.safety.entity.InvestigationHistory;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import dev.andre.accidentalert.safety.mapper.InvestigationMapper;
import dev.andre.accidentalert.safety.repository.InvestigationHistoryRepository;
import dev.andre.accidentalert.safety.repository.InvestigationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class InvestigationService {

    // Map of valid status transitions. Each key is a current status, and the value is a set of valid next statuses.
    private static final Map<InvestigationStatus, Set<InvestigationStatus>> VALID_TRANSITIONS = Map.of(
            InvestigationStatus.OPEN, Set.of(InvestigationStatus.IN_ANALYSIS),
            InvestigationStatus.IN_ANALYSIS, Set.of(InvestigationStatus.ACTION_REQUIRED, InvestigationStatus.RESOLVED),
            InvestigationStatus.ACTION_REQUIRED, Set.of(InvestigationStatus.RESOLVED),
            InvestigationStatus.RESOLVED, Set.of(InvestigationStatus.CLOSED),
            InvestigationStatus.CLOSED, Set.of()
    );

    private final InvestigationRepository investigationRepository;
    private final UserRepository userRepository;
    private final AccidentRepository accidentRepository;
    private final InvestigationHistoryRepository investigationHistoryRepository;

    public InvestigationResponseDTO create(InvestigationRequestDTO dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is deactivated");
        }

        Accident accident = accidentRepository.findById(dto.accidentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Accident not found"));
        if (investigationRepository.existsByAccident(accident))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Accident is already in use");

        Investigation investigation = Investigation.builder()
                .accident(accident)
                .assignedTechnician(user)
                .rootCause(dto.rootCause())
                .observation(dto.observation())
                .status(dto.status())
                .build();

        Investigation saved = investigationRepository.save(investigation);

        return InvestigationMapper.toResponseDTO(saved);
    }

    public InvestigationResponseDTO findById(Long id) {
        Investigation investigation = investigationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investigation not found"));

        return InvestigationMapper.toResponseDTO(investigation);
    }

    public List<InvestigationResponseDTO> findAll() {
        List<Investigation> investigations = investigationRepository.findAll();

        return investigations.stream()
                .map(InvestigationMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public InvestigationResponseDTO updateStatus(Long id, InvestigationStatus newStatus, String observation) {

        Investigation investigation = investigationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investigation not found"));

        validateStatusTransition(investigation.getStatus(), newStatus);
        validateRoleTransition(newStatus);

        InvestigationStatus oldStatus = investigation.getStatus();

        investigation.setStatus(newStatus);
        investigation.setObservation(investigation.getObservation() + "\n - Status updated to: " + newStatus);
        Investigation saved = investigationRepository.save(investigation);
        InvestigationHistory history = InvestigationHistory.builder()
                .investigation(investigation)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .comment(observation)
                .changedBy(investigation.getAssignedTechnician())
                .build();

        investigationHistoryRepository.save(history);

        return InvestigationMapper.toResponseDTO(saved);
    }

    /**
     * Validates if the status transition is allowed.
     */
    private void validateStatusTransition(InvestigationStatus currentStatus, InvestigationStatus newStatus) {
        if (currentStatus == newStatus) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Investigation is already in the specified status");
        }

        Set<InvestigationStatus> validNextStatuses = VALID_TRANSITIONS.get(currentStatus);

        if (validNextStatuses == null || !validNextStatuses.contains(newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid status transition from " + currentStatus + " to " + newStatus);
        }

    }

    /**
     * Only SAFETY_MANAGER can turn investigation CLOSED.
     */
    private void validateRoleTransition(InvestigationStatus newStatus) {
        // Only to status CLOSED, validate is necessary to be SAFETY_MANAGER
        if (newStatus != InvestigationStatus.CLOSED) {
            return;
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        if (user.getRole() != Role.SAFETY_MANAGER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only safety managers can set investigation status to CLOSED");
        }
    }


    public List<InvestigationResponseDTO> findByStatus(InvestigationStatus status) {
        List<Investigation> investigationList = investigationRepository.findInvestigationByStatus(status);
        return investigationList.stream()
                .map(InvestigationMapper::toResponseDTO)
                .toList();
    }

}
