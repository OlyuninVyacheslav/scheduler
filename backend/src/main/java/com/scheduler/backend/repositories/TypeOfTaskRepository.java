package com.scheduler.backend.repositories;

import com.scheduler.backend.entities.TypeOfTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeOfTaskRepository extends JpaRepository<TypeOfTask, Long> {
    @Query("SELECT t FROM TypeOfTask t WHERE t.board.id = :boardId")
    List<TypeOfTask> findByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT MAX(t.order) FROM TypeOfTask t WHERE t.board.id = :boardId")
    Integer findMaxOrder(Long boardId);
}
