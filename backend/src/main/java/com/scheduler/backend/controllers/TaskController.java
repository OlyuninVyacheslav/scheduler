package com.scheduler.backend.controllers;

import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.dtos.*;
import com.scheduler.backend.entities.Task;
import com.scheduler.backend.entities.TypeOfTask;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.TaskRepository;
import com.scheduler.backend.repositories.TypeOfTaskRepository;
import com.scheduler.backend.repositories.UserRepository;
import com.scheduler.backend.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/board")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final TypeOfTaskRepository typeOfTaskRepository;
    private final UserRepository userRepository;
    private final UserAuthenticationProvider userAuthenticationProvider;


    @Autowired
    public TaskController(TaskService taskService, TaskRepository taskRepository, TypeOfTaskRepository typeOfTaskRepository, UserRepository userRepository, UserAuthenticationProvider userAuthenticationProvider) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.typeOfTaskRepository = typeOfTaskRepository;
        this.userRepository = userRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @GetMapping("/type/tasks/{typeId}")
    public ResponseEntity<List<TaskDto>> getTasksByType(@PathVariable Long typeId, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            List<TaskDto> tasks = taskService.getTasksByType(typeId);
            return ResponseEntity.ok(tasks);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/type/task/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            TypeOfTask taskType = typeOfTaskRepository.findById(taskDto.getTypeId())
                    .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + taskDto.getTypeId()));
            taskDto.setTypeId(taskType.getId());
            TaskDto createdTask = taskService.createTask(taskDto);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (NoSuchElementException | EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>("Error: Invalid or expired token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/type/task/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable String taskId, @RequestBody TaskDto taskDto, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            TaskDto updatedTask = taskService.updateTask(taskId, taskDto);
            return ResponseEntity.ok(updatedTask);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/type/task/move")
    public ResponseEntity<Void> moveTask(@RequestBody MoveTaskRequest moveTaskRequest, @RequestHeader("Authorization") String token) {
        try {
            taskService.moveTask(moveTaskRequest.getTaskId(), moveTaskRequest.getSourceTypeId(), moveTaskRequest.getDestinationTypeId(), moveTaskRequest.getNewOrder());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/type/tasks/dnd")
    public ResponseEntity<Void> updateTasksInType(@RequestBody List<TaskDto> tasks, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            for (TaskDto taskDto : tasks) {
                Task task = taskRepository.findById(taskDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskDto.getId()));
                task.setOrder(taskDto.getOrder().intValue());
                TypeOfTask taskType = typeOfTaskRepository.findById(taskDto.getTypeId())
                        .orElseThrow(() -> new NoSuchElementException("TaskType not found with id: " + taskDto.getTypeId()));
                task.setTypeId(taskType);
                taskRepository.save(task);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tasks/move")
    public ResponseEntity<Void> moveTasks(@RequestHeader("Authorization") String token, @RequestBody Map<String, List<MoveTaskRequest>> moveTasks) {
        try {
            List<MoveTaskRequest> taskRequests = moveTasks.get("moveTasks");
            taskService.moveTasks(taskRequests);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/type/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        try {
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
