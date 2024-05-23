package com.scheduler.backend.services;
//
//import com.scheduler.backend.dtos.TaskDto;
//import com.scheduler.backend.entities.Task;
//import com.scheduler.backend.entities.TypeOfTask;
//import com.scheduler.backend.entities.User;
//import com.scheduler.backend.repositories.TaskRepository;
//import com.scheduler.backend.repositories.TypeOfTaskRepository;
//import com.scheduler.backend.repositories.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class TaskService {
//    private final TaskRepository taskRepository;
//    private final TypeOfTaskRepository taskTypeRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public TaskService(TaskRepository taskRepository, TypeOfTaskRepository taskTypeRepository, UserRepository userRepository) {
//        this.taskRepository = taskRepository;
//        this.taskTypeRepository = taskTypeRepository;
//        this.userRepository = userRepository;
//    }
//
//    public List<TaskDto> getTasksByType(Long typeId) {
//        List<Task> tasks = taskRepository.findByTaskTypeId(typeId);
//        return tasks.stream().map(this::convertToDto).collect(Collectors.toList());
//    }
//
//    public TaskDto createTask(TaskDto taskDto, User creator) {
//        TypeOfTask taskType = taskTypeRepository.findById(taskDto.getTypeId().getId())
//                .orElseThrow(() -> new EntityNotFoundException("TaskType not found with id: " + taskDto.getTypeId().getId()));
//        Task task = new Task();
//        task.setName(taskDto.getName());
//        task.setDescription(taskDto.getDescription());
//        task.setType(taskType);
//        task.setDeadline(taskDto.getDeadline());
//        Task savedTask = taskRepository.save(task);
//        return convertToDto(savedTask);
//    }
//
//    public TaskDto updateTask(Long taskId, TaskDto taskDto) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
//        task.setName(taskDto.getName());
//        task.setDescription(taskDto.getDescription());
//        Task updatedTask = taskRepository.save(task);
//        return convertToDto(updatedTask);
//    }
//
//    public TaskDto moveTask(Long taskId, Long toTypeId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
//        TypeOfTask newTaskType = taskTypeRepository.findById(toTypeId)
//                .orElseThrow(() -> new EntityNotFoundException("TaskType not found with id: " + toTypeId));
//        task.setType(newTaskType);
//        Task movedTask = taskRepository.save(task);
//        return convertToDto(movedTask);
//    }
//
//    public void deleteTask(Long taskId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
//        taskRepository.delete(task);
//    }
//
//    private TaskDto convertToDto(Task task) {
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(task.getId());
//        taskDto.setName(task.getName());
//        taskDto.setDescription(task.getDescription());
//        taskDto.setTypeId(task.getType());
//        taskDto.setDeadline(task.getDeadline());
//        return taskDto;
//    }
//}

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
        task.setDescription(taskDto.getDescription());
        task.setTaskTypeId(taskType);
        task.setDeadline(taskDto.getDeadline());

        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }

    public TaskDto updateTask(Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

        modelMapper.map(taskDto, task);
        task.setDeadline(taskDto.getDeadline());

        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskDto.class);
    }

    public TaskDto moveTask(Long taskId, Long toTypeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

        TypeOfTask newTaskType = taskTypeRepository.findById(toTypeId)
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + toTypeId));

        task.setTaskTypeId(newTaskType);

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
}
