package com.scheduler.backend.entities;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Getter
    @Setter
    @Size(max = 255)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Getter
    @Setter
    @ManyToOne // assuming FetchType.LAZY for better performance
    @JoinColumn(name = "type_id")
    private TypeOfTask typeId;

    @Column(name = "description", columnDefinition = "text")
    @Getter
    @Setter
    private String description;

    @Column(name = "deadline")
    @Getter
    @Setter
    private LocalDate deadline;

    @Column(name = "order_")
    @Getter
    @Setter
    private Integer order;

    @Column(name = "created_at")
    @Getter
    @Setter
    private LocalDateTime createdAt;

    // Constructors, getters, setters can be generated by Lombok @Data annotation


}