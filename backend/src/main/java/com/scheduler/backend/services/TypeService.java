package com.scheduler.backend.services;

import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.entities.Board;
import com.scheduler.backend.entities.Task;
import com.scheduler.backend.entities.TypeOfTask;
import com.scheduler.backend.repositories.BoardRepository;
import com.scheduler.backend.repositories.TaskRepository;
import com.scheduler.backend.repositories.TypeOfTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class TypeService {
    private final TaskRepository taskRepository;
    private final TypeOfTaskRepository taskTypeRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    public TypeService(TaskRepository taskRepository, TypeOfTaskRepository taskTypeRepository, ModelMapper modelMapper, BoardRepository boardRepository) {
        this.taskRepository = taskRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.modelMapper = modelMapper;
        this.boardRepository = boardRepository;
    }

    public List<TypeOfTaskDto> getTypesByBoardId(Long boardId) {
        List<TypeOfTask> types = taskTypeRepository.findByBoardId(boardId);
        return types.stream()
                .map(type -> new TypeOfTaskDto(type.getId(), type.getName(), type.getOrder()))
                .collect(Collectors.toList());
    }

    public TypeOfTaskDto createType(Long boardId, TypeOfTaskDto taskTypeDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        TypeOfTask taskTypeEntity = new TypeOfTask();
        taskTypeEntity.setName(taskTypeDto.getName());
        taskTypeEntity.setBoard(board);
        Integer maxOrder = taskTypeRepository.findMaxOrder(boardId);
        taskTypeEntity.setOrder(maxOrder != null ? maxOrder + 1 : 0);
        TypeOfTask savedTaskType = taskTypeRepository.save(taskTypeEntity);
        TypeOfTaskDto createdTaskTypeDto = new TypeOfTaskDto();
        createdTaskTypeDto.setName(savedTaskType.getName());
        return createdTaskTypeDto;
    }

    public void moveTypes(List<TypeOfTaskDto> updatedTypes) {
        for (TypeOfTaskDto updatedType : updatedTypes) {
            TypeOfTask type = taskTypeRepository.findById(updatedType.getId())
                    .orElseThrow(() -> new NoSuchElementException("BoardType not found with id: " + updatedType.getId()));
            type.setOrder(updatedType.getOrder());
            taskTypeRepository.save(type);
        }
    }

    public TypeOfTaskDto updateType(Long typeId, TypeOfTaskDto updatedTypeDto) {
        TypeOfTask type = taskTypeRepository.findById(typeId)
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + typeId));
        type.setName(updatedTypeDto.getName());
        TypeOfTask updatedType = taskTypeRepository.save(type);
        return modelMapper.map(updatedType, TypeOfTaskDto.class);
    }

    public void deleteType(Long typeId) {
        List<Task> tasks = taskRepository.findByTaskTypeId(typeId);
        taskRepository.deleteAll(tasks);
        taskTypeRepository.deleteById(typeId);
    }
}
