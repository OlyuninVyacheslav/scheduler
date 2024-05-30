package com.scheduler.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoveTaskRequest {
    private String taskId;
    private Long sourceTypeId;
    private Long destinationTypeId;
    private Integer newOrder;
}
