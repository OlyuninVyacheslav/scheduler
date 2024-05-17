package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.TypeOfTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeOfTaskRepository extends JpaRepository<TypeOfTask, Long> {
}
