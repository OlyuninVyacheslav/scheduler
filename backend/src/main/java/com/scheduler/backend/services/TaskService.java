package com.scheduler.backend.services;

import com.scheduler.backend.dtos.TaskDto;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.entities.Task;
import com.scheduler.backend.entities.TypeOfTask;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.TaskRepository;
import com.scheduler.backend.repositories.TypeOfTaskRepository;
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

    @Autowired
    public TaskService(TaskRepository taskRepository, TypeOfTaskRepository taskTypeRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);

        TypeOfTask taskType = taskTypeRepository.findById(taskDto.getTypeId().getId())
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + taskDto.getTypeId()));
        task.setName(taskDto.getName());
        task.setCreatedAt(LocalDateTime.now());
        task.setDescription(taskDto.getDescription());
        task.setTypeId(taskType);
        task.setDeadline(taskDto.getDeadline());

        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }

public TaskDto updateTask(Long taskId, TaskDto taskDto) {
    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

    // Ensure the task type exists before setting it
    if (taskDto.getTypeId() != null && taskDto.getTypeId().getId() != null) {
        TypeOfTask taskType = taskTypeRepository.findById(taskDto.getTypeId().getId())
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + taskDto.getTypeId().getId()));
        task.setTypeId(taskType);
    }

    // Update other task fields
    task.setName(taskDto.getName());
    task.setDescription(taskDto.getDescription());
    task.setDeadline(taskDto.getDeadline());

    Task updatedTask = taskRepository.save(task);
    return modelMapper.map(updatedTask, TaskDto.class);
}


    public TaskDto moveTask(Long taskId, Long toTypeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

        TypeOfTask newTaskType = taskTypeRepository.findById(toTypeId)
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + toTypeId));

        task.setTypeId(newTaskType);

        Task movedTask = taskRepository.save(task);
        return modelMapper.map(movedTask, TaskDto.class);
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

        taskRepository.delete(task);
    }

    public List<TaskDto> getTasksByType(Long typeId) {
        List<Task> tasks = taskRepository.findByTaskTypeId(typeId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }

    public TypeOfTaskDto createTaskType(Long boardId, TypeOfTaskDto taskTypeDto) {
        // Дополнительная логика валидации и сохранения типа задачи
        TypeOfTask taskTypeEntity = new TypeOfTask();
        taskTypeEntity.setName(taskTypeDto.getName());
        // Установка связей с другими сущностями, если необходимо
        // Например, boardId можно использовать для связи с доской

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
