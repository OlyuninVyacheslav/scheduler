package com.scheduler.backend.dtos;
import com.scheduler.backend.entities.Task;
import com.scheduler.backend.entities.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskUserDto {
    private User userId;
    private Task taskId;
}
