package com.scheduler.backend.dtos;

import com.scheduler.backend.entities.Task;
import com.scheduler.backend.entities.TypeOfTask;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private String id;

    @NotBlank
    @Size(max = 255)
    private String name;

    private Long typeId;

    private String description;

    private LocalDate deadline;

    private Integer order;

    private LocalDateTime createdAt;
}