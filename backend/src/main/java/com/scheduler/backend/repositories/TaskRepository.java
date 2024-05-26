package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.Task;
import com.scheduler.backend.entities.TypeOfTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
//    List<Task> findByTaskTypeId(Long taskTypeId);
    @Query("SELECT t FROM Task t WHERE t.typeId.id = :typeId")
    List<Task> findByTaskTypeId(@Param("typeId") Long typeId);

    @Query("SELECT MAX(t.order) FROM Task t WHERE t.typeId.id = :typeId")
    Integer findMaxOrderByTypeId(@Param("typeId") Long typeId);

}
