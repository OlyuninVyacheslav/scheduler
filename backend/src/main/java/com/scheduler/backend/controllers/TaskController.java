package com.scheduler.backend.controllers;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.dtos.TaskDto;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.dtos.UserDto;
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
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }



    @GetMapping("/types/{boardId}")
    public ResponseEntity<List<TypeOfTaskDto>> getTaskTypesByBoardId(@PathVariable Long boardId, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            List<TypeOfTaskDto> taskTypes = taskService.getTaskTypesByBoardId(boardId);
            return ResponseEntity.ok(taskTypes);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    //region TaskType
    @PostMapping("/{boardId}/type")
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
    @PutMapping("/type/{typeId}")
    public ResponseEntity<TypeOfTaskDto> updateTaskType(@PathVariable Long typeId, @RequestBody TypeOfTaskDto updatedTypeDto, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            TypeOfTaskDto updatedType = taskService.updateTaskType(typeId, updatedTypeDto);
            return ResponseEntity.ok(updatedType);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/type/{typeId}")
    public ResponseEntity<Void> deleteTaskType(@PathVariable Long typeId, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            taskService.deleteTaskType(typeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //endregion

    //region Task
//    @PostMapping("/type/task/create")
//    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto, @RequestHeader("Authorization") String token) {
//        try {
//            String jwt = token.substring(7);
//            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
//            TaskDto createdTask = taskService.createTask(taskDto);
//            System.out.println(createdTask);
//            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }
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
public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskDto, @RequestHeader("Authorization") String token) {
    try {
        String jwt = token.substring(7);
        Authentication authentication = userAuthenticationProvider.validateToken(jwt);
        Long creatorId = ((UserDto) authentication.getPrincipal()).getId();
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + creatorId));

        TaskDto updatedTask = taskService.updateTask(taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    } catch (EntityNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (NoSuchElementException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // or a more specific status
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // for unexpected errors
    }
}


    @PutMapping("/type/task/{taskId}/move")
    public ResponseEntity<TaskDto> moveTask(@PathVariable Long taskId, @RequestParam Long toTypeId) {
        try {
            TaskDto movedTask = taskService.moveTask(taskId, toTypeId);
            return ResponseEntity.ok(movedTask);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/type/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //endregion
}
