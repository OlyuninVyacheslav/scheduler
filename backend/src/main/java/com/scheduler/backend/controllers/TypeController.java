package com.scheduler.backend.controllers;

import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.repositories.TaskRepository;
import com.scheduler.backend.repositories.TypeOfTaskRepository;
import com.scheduler.backend.repositories.UserRepository;
import com.scheduler.backend.services.TypeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class TypeController {
    private final TypeService typeService;
    private final TaskRepository taskRepository;
    private final TypeOfTaskRepository typeOfTaskRepository;
    private final UserRepository userRepository;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    public TypeController(TypeService typeService, TaskRepository taskRepository, TypeOfTaskRepository typeOfTaskRepository, UserRepository userRepository, UserAuthenticationProvider userAuthenticationProvider) {
        this.typeService = typeService;
        this.taskRepository = taskRepository;
        this.typeOfTaskRepository = typeOfTaskRepository;
        this.userRepository = userRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @GetMapping("/types/{boardId}")
    public ResponseEntity<List<TypeOfTaskDto>> getTypesByBoardId(@PathVariable Long boardId, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            List<TypeOfTaskDto> taskTypes = typeService.getTypesByBoardId(boardId);
            return ResponseEntity.ok(taskTypes);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/{boardId}/type")
    public ResponseEntity<TypeOfTaskDto> createType(@PathVariable Long boardId, @RequestBody TypeOfTaskDto taskTypeDto, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            TypeOfTaskDto createdTaskType = typeService.createType(boardId, taskTypeDto);
            return new ResponseEntity<>(createdTaskType, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/types/move")
    public ResponseEntity<String> moveTypes(@RequestBody List<TypeOfTaskDto> updatedTypes) {
        try {
            typeService.moveTypes(updatedTypes);
            return ResponseEntity.ok("Types moved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to move types: " + e.getMessage());
        }
    }

    @PutMapping("/type/{typeId}")
    public ResponseEntity<TypeOfTaskDto> updateType(@PathVariable Long typeId, @RequestBody TypeOfTaskDto updatedTypeDto, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            TypeOfTaskDto updatedType = typeService.updateType(typeId, updatedTypeDto);
            return ResponseEntity.ok(updatedType);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/type/{typeId}")
    public ResponseEntity<Void> deleteType(@PathVariable Long typeId, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            typeService.deleteType(typeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
