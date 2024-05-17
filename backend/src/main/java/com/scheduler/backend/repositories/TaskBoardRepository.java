package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.TaskBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long> {
}
