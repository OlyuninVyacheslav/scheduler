package com.scheduler.backend.dtos;
import lombok.*;

import jakarta.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;
}
