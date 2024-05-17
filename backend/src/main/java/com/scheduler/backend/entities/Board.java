package com.scheduler.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors

    public Board(String name, User creator, LocalDateTime createdAt) {
        this.name = name;
        this.creator = creator;
        this.createdAt = createdAt; // Automatically set creation timestamp
    }

    // Getters and setters
}
