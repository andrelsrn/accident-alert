package dev.andre.accidentalert.safety.entity;

import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.safety.entity.safetyEnums.InvestigationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "investigation_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestigationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investigation_id", nullable = false)
    private Investigation investigation;

    @Enumerated(EnumType.STRING)
    private InvestigationStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private InvestigationStatus newStatus;


    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "change_by_user_id", nullable = false)
    private User changedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}


