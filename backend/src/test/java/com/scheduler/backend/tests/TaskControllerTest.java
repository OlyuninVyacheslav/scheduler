package com.scheduler.backend.tests;

import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.controllers.TaskController;
import com.scheduler.backend.dtos.TaskDto;
import com.scheduler.backend.services.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void testGetTasksByType() {
        String token = userAuthenticationProvider.createToken("")
        // Arrange
        Long typeId = 1L;
        List<TaskDto> expectedTasks = new ArrayList<>();
        TaskDto taskDto = TaskDto.builder()
                .id(1L)
                .name("Test Task")
                .typeId(1L)
                .description("Test task description")
                .deadline(LocalDate.now())
                .order(1)
                .createdAt(LocalDateTime.now())
                .build();
        expectedTasks.add(taskDto);

        // Мокирование зависимостей
        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(taskService.getTasksByType(typeId)).thenReturn(expectedTasks);

        // Act
        ResponseEntity<List<TaskDto>> response = taskController.getTasksByType(typeId, "Bearer mockToken");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTasks, response.getBody());
        verify(taskService, times(1)).getTasksByType(typeId);
    }
}
