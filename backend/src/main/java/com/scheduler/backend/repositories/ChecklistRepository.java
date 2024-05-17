package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
}
