package dev.andre.accidentalert.entity;

import dev.andre.accidentalert.entity.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(nullable = false)
    private String victimName;

    @Column(nullable = false)
    private String victimDepartment;
}
