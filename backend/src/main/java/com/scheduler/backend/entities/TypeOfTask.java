package com.scheduler.backend.entities;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "type_list")
public class TypeOfTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false, length = 255)
    @Getter
    @Setter
    private String name;

    @Column(name = "order_")
    @Getter
    @Setter
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}