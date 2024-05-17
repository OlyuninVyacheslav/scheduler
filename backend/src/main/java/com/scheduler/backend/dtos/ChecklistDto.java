package com.scheduler.backend.dtos;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecklistDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    private LocalDate deadline;

    private LocalDateTime createdAt;

    // Constructors, getters, setters can be generated by Lombok @Data annotation
}