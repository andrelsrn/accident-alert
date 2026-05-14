package dev.andre.accidentalert.safety.entity;

import dev.andre.accidentalert.ambulatory.entity.Accident;
import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "investigations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "accident_id")
    private Accident accident;

    @ManyToOne
    @JoinColumn(name = "assigned_technician_id", nullable = false)
    private User assignedTechnician;

    @Column(nullable = true)
    private String rootCause;

    @Column(nullable = false)
    private String observation;

    @Enumerated(EnumType.STRING)
    private InvestigationStatus status;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    }


