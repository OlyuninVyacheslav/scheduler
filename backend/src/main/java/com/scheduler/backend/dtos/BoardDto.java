package com.scheduler.backend.dtos;

import com.scheduler.backend.entities.User;
import lombok.*;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    private User creator;

    private LocalDateTime createdAt;

    // Constructors, getters, setters can be generated by Lombok @Data annotation
}
