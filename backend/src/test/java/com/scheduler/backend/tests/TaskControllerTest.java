package com.scheduler.backend.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.controllers.TaskController;
import com.scheduler.backend.dtos.MoveTaskRequest;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.services.TaskService;
import com.scheduler.backend.services.TypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TypeService typeService;

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
        when(typeService.createType(anyLong(), any(TypeOfTaskDto.class)))
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
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void moveTypes_ReturnsOk_WhenValidInput() throws Exception {
        // Arrange
        List<TypeOfTaskDto> updatedTypes = Collections.emptyList();
        doNothing().when(typeService).moveTypes(anyList());

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(put("/board/types/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTypes))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTaskType_ReturnsUpdatedTaskType_WhenValidInput() throws Exception {
        // Arrange
        TypeOfTaskDto inputDto = TypeOfTaskDto.builder()
                .name("Updated Type")
                .order(2)
                .build();
        TypeOfTaskDto expectedDto = TypeOfTaskDto.builder()
                .id(1L)
                .name("Updated Type")
                .order(2)
                .build();
        when(typeService.updateType(anyLong(), any(TypeOfTaskDto.class)))
                .thenReturn(expectedDto);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(put("/board/type/{typeId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputDto))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Type"))
                .andExpect(jsonPath("$.order").value(2));
    }

    @Test
    void deleteTaskType_ReturnsNoContent_WhenValidTypeId() throws Exception {
        // Arrange
        doNothing().when(typeService).deleteType(anyLong());

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(delete("/board/type/{typeId}", 1)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }


    @Test
    void moveTask_ReturnsNoContent_WhenValidInput() throws Exception {
        // Arrange
        MoveTaskRequest moveTaskRequest = new MoveTaskRequest("1", 1L, 2L, 1);
        doNothing().when(taskService).moveTask(anyString(), anyLong(), anyLong(), anyInt());

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(put("/board/type/task/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(moveTaskRequest))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void moveTasks_ReturnsNoContent_WhenValidInput() throws Exception {
        // Arrange
        Map<String, List<MoveTaskRequest>> moveTasks = Collections.singletonMap("moveTasks", Collections.emptyList());
        doNothing().when(taskService).moveTasks(anyList());

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(put("/board/tasks/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(moveTasks))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_ReturnsNoContent_WhenValidTaskId() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(anyString());

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        // Act & Assert
        mockMvc.perform(delete("/board/type/task/{taskId}", "1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }


    // Add more tests for other controller methods similarly
}