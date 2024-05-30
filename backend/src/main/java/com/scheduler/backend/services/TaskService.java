package com.scheduler.backend.services;

import com.scheduler.backend.dtos.TaskDto;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.entities.Board;
import com.scheduler.backend.entities.Task;
import com.scheduler.backend.entities.TypeOfTask;
import com.scheduler.backend.repositories.BoardRepository;
import com.scheduler.backend.repositories.TaskRepository;
import com.scheduler.backend.repositories.TaskUserRepository;
import com.scheduler.backend.repositories.TypeOfTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TypeOfTaskRepository taskTypeRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TypeOfTaskRepository taskTypeRepository, ModelMapper modelMapper, BoardRepository boardRepository                       ) {
        this.taskRepository = taskRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.modelMapper = modelMapper;
        this.boardRepository = boardRepository;
        //this.taskUserRepository = taskUserRepository;
    }


    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);

        TypeOfTask taskType = taskTypeRepository.findById(taskDto.getTypeId())
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + taskDto.getTypeId()));

        task.setName(taskDto.getName());
        task.setCreatedAt(LocalDateTime.now());
        task.setDescription(taskDto.getDescription());
        task.setTypeId(taskType);
        task.setDeadline(taskDto.getDeadline());

        Integer maxOrder = taskRepository.findMaxOrderByTypeId(taskDto.getTypeId());
        task.setOrder(maxOrder != null ? maxOrder + 1 : 0);

        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }

public TaskDto updateTask(Long taskId, TaskDto taskDto) {
    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

    // Ensure the task type exists before setting it
    if (taskDto.getTypeId() != null && taskDto.getTypeId() != null) {
        TypeOfTask taskType = taskTypeRepository.findById(taskDto.getTypeId())
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + taskDto.getTypeId()));
        task.setTypeId(taskType);
    }

    // Update other task fields
    task.setName(taskDto.getName());
    task.setDescription(taskDto.getDescription());
    task.setDeadline(taskDto.getDeadline());

    Task updatedTask = taskRepository.save(task);
    return modelMapper.map(updatedTask, TaskDto.class);
}

//    public TaskDto moveTask(Long taskId, Long toTypeId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));
//
//        TypeOfTask newTaskType = taskTypeRepository.findById(toTypeId)
//                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + toTypeId));
//
//        task.setTypeId(newTaskType);
//
//        Task movedTask = taskRepository.save(task);
//        return modelMapper.map(movedTask, TaskDto.class);
//    }
public void moveTask(Long taskId, Long sourceTypeId, Long destinationTypeId, Integer newOrder) {
    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

    TypeOfTask destinationType = taskTypeRepository.findById(destinationTypeId)
            .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + destinationTypeId));

    task.setTypeId(destinationType);
    task.setOrder(newOrder);

    taskRepository.save(task);

    // Update orders in source and destination types
    List<Task> sourceTasks = taskRepository.findByTaskTypeId(sourceTypeId)
            .stream()
            .filter(t -> !t.getId().equals(taskId))
            .sorted((a, b) -> a.getOrder().compareTo(b.getOrder()))
            .collect(Collectors.toList());

    for (int i = 0; i < sourceTasks.size(); i++) {
        sourceTasks.get(i).setOrder(i);
        taskRepository.save(sourceTasks.get(i));
    }

    List<Task> destinationTasks = taskRepository.findByTaskTypeId(destinationTypeId)
            .stream()
            .sorted((a, b) -> a.getOrder().compareTo(b.getOrder()))
            .collect(Collectors.toList());

    for (int i = 0; i < destinationTasks.size(); i++) {
        destinationTasks.get(i).setOrder(i);
        taskRepository.save(destinationTasks.get(i));
    }
}


    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

        taskRepository.delete(task);
    }


public List<TaskDto> getTasksByType(Long typeId) {
    List<Task> tasks = taskRepository.findByTaskTypeId(typeId);
    return tasks.stream()
            .map(task -> {
                TaskDto taskDto = modelMapper.map(task, TaskDto.class);
                taskDto.setTypeId(task.getTypeId().getId());
                return taskDto;
            })
            .collect(Collectors.toList());
}
    public List<TypeOfTaskDto> getTaskTypesByBoardId(Long boardId) {
        List<TypeOfTask> types = taskTypeRepository.findByBoardId(boardId);
        return types.stream()
                .map(type -> new TypeOfTaskDto(type.getId(), type.getName(), type.getOrder()))
                .collect(Collectors.toList());
    }

    public TypeOfTaskDto createTaskType(Long boardId, TypeOfTaskDto taskTypeDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        // Дополнительная логика валидации и сохранения типа задачи
        TypeOfTask taskTypeEntity = new TypeOfTask();
        taskTypeEntity.setName(taskTypeDto.getName());
        // Установка связей с другими сущностями, если необходимо
        // Например, boardId можно использовать для связи с доской
        taskTypeEntity.setBoard(board);
        Integer maxOrder = taskTypeRepository.findMaxOrder(boardId);

        // Устанавливаем порядковый номер нового типа задачи
        taskTypeEntity.setOrder(maxOrder != null ? maxOrder + 1 : 0);
        // Сохранение в базе данных
        TypeOfTask savedTaskType = taskTypeRepository.save(taskTypeEntity);

        // Возврат DTO созданного типа задачи
        TypeOfTaskDto createdTaskTypeDto = new TypeOfTaskDto();
        createdTaskTypeDto.setName(savedTaskType.getName());
        // Другие поля, если необходимо

        return createdTaskTypeDto;
    }

    public TypeOfTaskDto updateTaskType(Long typeId, TypeOfTaskDto updatedTypeDto) {
        TypeOfTask type = taskTypeRepository.findById(typeId)
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + typeId));

        type.setName(updatedTypeDto.getName()); // Обновляем имя типа задачи

        TypeOfTask updatedType = taskTypeRepository.save(type); // Сохраняем обновленный тип задачи

        return modelMapper.map(updatedType, TypeOfTaskDto.class);
    }

    public void deleteTaskType(Long typeId) {
        // Находим все задачи, связанные с данным типом задачи
        List<Task> tasks = taskRepository.findByTaskTypeId(typeId);

        // Удаляем найденные задачи
        taskRepository.deleteAll(tasks);

        // Удаляем сам тип задачи
        taskTypeRepository.deleteById(typeId);
    }






}
