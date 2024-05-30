package com.scheduler.backend.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.controllers.TaskController;
import com.scheduler.backend.dtos.TaskDto;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @Test
    void getTasksByType_ReturnsTasks_WhenValidTypeId() throws Exception {
        // Arrange
        when(taskService.getTasksByType(anyLong())).thenReturn(Collections.emptyList());
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(get("/board/type/tasks/{typeId}", 1)
                        .header("Authorization", "Bearer auth_token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createTaskType_ReturnsCreatedTaskType_WhenValidInput() throws Exception {
        // Arrange
        TypeOfTaskDto inputDto = TypeOfTaskDto.builder()
                .name("Test Type")
                .order(1)
                .build();
        TypeOfTaskDto expectedDto = TypeOfTaskDto.builder()
                .id(1L)
                .name("Test Type")
                .order(1)
                .build();
        when(taskService.createTaskType(anyLong(), any(TypeOfTaskDto.class)))
                .thenReturn(expectedDto);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(post("/board/{boardId}/type", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputDto))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Type"))
                .andExpect(jsonPath("$.order").value(1));
    }

    // Преобразование объекта в JSON строку для запроса
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Add more tests for other controller methods similarly
}