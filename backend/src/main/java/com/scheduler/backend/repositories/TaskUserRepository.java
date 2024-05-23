package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser, Long> {
}
