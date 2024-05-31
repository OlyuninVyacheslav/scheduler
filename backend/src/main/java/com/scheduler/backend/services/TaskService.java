package com.scheduler.backend.services;

import com.scheduler.backend.dtos.MoveTaskRequest;
import com.scheduler.backend.dtos.TaskDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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

    public TaskDto updateTask(String taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));


        if (taskDto.getTypeId() != null && taskDto.getTypeId() != null) {
            TypeOfTask taskType = taskTypeRepository.findById(taskDto.getTypeId())
                    .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + taskDto.getTypeId()));
            task.setTypeId(taskType);
        }

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(taskDto.getDeadline());

        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskDto.class);
    }

    public void moveTasks(List<MoveTaskRequest> taskRequests) {
        for (MoveTaskRequest moveRequest : taskRequests) {
            moveTask(moveRequest.getTaskId(), moveRequest.getSourceTypeId(), moveRequest.getDestinationTypeId(), moveRequest.getNewOrder());
        }
        updateTaskOrders(taskRequests);
    }

    public void moveTask(String taskId, Long sourceTypeId, Long destinationTypeId, Integer newOrder) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskId));

        TypeOfTask destinationType = taskTypeRepository.findById(destinationTypeId)
                .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + destinationTypeId));

        task.setTypeId(destinationType);
        task.setOrder(newOrder);

        taskRepository.save(task);
    }

    private void updateTaskOrders(List<MoveTaskRequest> taskRequests) {
        Map<Long, List<String>> sourceTypeTasks = new HashMap<>();
        Map<Long, List<String>> destinationTypeTasks = new HashMap<>();

        for (MoveTaskRequest request : taskRequests) {
            sourceTypeTasks.computeIfAbsent(request.getSourceTypeId(), k -> new ArrayList<>()).add(request.getTaskId());
            destinationTypeTasks.computeIfAbsent(request.getDestinationTypeId(), k -> new ArrayList<>()).add(request.getTaskId());
        }

        // Обновляем порядок задач в исходном типе
        for (Long sourceTypeId : sourceTypeTasks.keySet()) {
            List<Task> sourceTasks = taskRepository.findByTaskTypeId(sourceTypeId)
                    .stream()
                    .filter(t -> !sourceTypeTasks.get(sourceTypeId).contains(t.getId()))
                    .sorted(Comparator.comparingInt(Task::getOrder))
                    .collect(Collectors.toList());

            for (int i = 0; i < sourceTasks.size(); i++) {
                sourceTasks.get(i).setOrder(i);
                taskRepository.save(sourceTasks.get(i));
            }
        }

        // Обновляем порядок задач в целевом типе
        for (Long destinationTypeId : destinationTypeTasks.keySet()) {
            List<Task> destinationTasks = taskRepository.findByTaskTypeId(destinationTypeId)
                    .stream()
                    .sorted(Comparator.comparingInt(Task::getOrder))
                    .collect(Collectors.toList());

            for (int i = 0; i < destinationTasks.size(); i++) {
                destinationTasks.get(i).setOrder(i);
                taskRepository.save(destinationTasks.get(i));
            }
        }
    }

    public void deleteTask(String taskId) {
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
}
