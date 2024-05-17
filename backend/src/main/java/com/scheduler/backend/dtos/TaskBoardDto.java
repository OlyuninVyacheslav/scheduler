package com.scheduler.backend.dtos;
import com.scheduler.backend.entities.Board;
import com.scheduler.backend.entities.Task;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskBoardDto {

    private Board boardId;
    private Task taskId;

    // Constructors, getters, setters can be generated by Lombok @Data annotation
}
