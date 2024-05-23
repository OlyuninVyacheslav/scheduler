package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.TypeOfTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfTaskRepository extends JpaRepository<TypeOfTask, Long> {
}
