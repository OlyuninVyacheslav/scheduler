package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.TaskBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long> {
}
