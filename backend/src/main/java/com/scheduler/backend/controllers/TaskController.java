package com.scheduler.backend.controllers;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.dtos.TaskDto;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.dtos.UserDto;
import com.scheduler.backend.entities.TypeOfTask;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.TaskRepository;
import com.scheduler.backend.repositories.UserRepository;
import com.scheduler.backend.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board/type")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    public TaskController(TaskService taskService, TaskRepository taskRepository, UserRepository userRepository, UserAuthenticationProvider userAuthenticationProvider) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<TypeOfTaskDto> createTaskType(@PathVariable Long boardId, @RequestBody TypeOfTaskDto taskTypeDto, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            TypeOfTaskDto createdTaskType = taskService.createTaskType(boardId, taskTypeDto);
            return new ResponseEntity<>(createdTaskType, HttpStatus.CREATED);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/tasks/{typeId}")
    public ResponseEntity<List<TaskDto>> getTasksByType(@PathVariable Long typeId, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            List<TaskDto> tasks = taskService.getTasksByType(typeId);
            return ResponseEntity.ok(tasks);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/task/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
//            Long creatorId = ((UserDto) authentication.getPrincipal()).getId();
//            User creator = userRepository.findById(creatorId)
//                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + creatorId));
            TaskDto createdTask = taskService.createTask(taskDto);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskDto) {
        try {
            TaskDto updatedTask = taskService.updateTask(taskId, taskDto);
            return ResponseEntity.ok(updatedTask);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/task/{taskId}/move")
    public ResponseEntity<TaskDto> moveTask(@PathVariable Long taskId, @RequestParam Long toTypeId) {
        try {
            TaskDto movedTask = taskService.moveTask(taskId, toTypeId);
            return ResponseEntity.ok(movedTask);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
