package com.scheduler.backend.dtos;

public class MoveTaskRequest {
    private Long taskId;
    private Long sourceTypeId;
    private Long destinationTypeId;
    private Integer newOrder;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(Long sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }

    public Long getDestinationTypeId() {
        return destinationTypeId;
    }

    public void setDestinationTypeId(Long destinationTypeId) {
        this.destinationTypeId = destinationTypeId;
    }

    public Integer getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Integer newOrder) {
        this.newOrder = newOrder;
    }
}
