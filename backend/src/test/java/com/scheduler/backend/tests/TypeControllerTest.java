package com.scheduler.backend.tests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.controllers.TypeController;
import com.scheduler.backend.dtos.TypeOfTaskDto;
import com.scheduler.backend.services.TypeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TypeControllerTest {

    @Mock
    private TypeService typeService;

    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;

    @InjectMocks
    private TypeController typeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(typeController).build();
    }

    @Test
    void getTypesByBoardId_ReturnsTypes_WhenValidBoardId() throws Exception {
        // Arrange
        Long boardId = 1L;
        List<TypeOfTaskDto> taskTypes = Collections.emptyList();
        String jwt = "token";

        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(typeService.getTypesByBoardId(boardId)).thenReturn(taskTypes);

        // Act & Assert
        mockMvc.perform(get("/board/types/{boardId}", boardId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTypesByBoardId_ReturnsNotFound_WhenEntityNotFound() throws Exception {
        // Arrange
        Long boardId = 1L;
        String jwt = "token";

        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(typeService.getTypesByBoardId(boardId)).thenThrow(EntityNotFoundException.class);

        // Act & Assert
        mockMvc.perform(get("/board/types/{boardId}", boardId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    void createType_ReturnsCreatedType_WhenValidInput() throws Exception {
        // Arrange
        Long boardId = 1L;
        TypeOfTaskDto inputDto = new TypeOfTaskDto();
        TypeOfTaskDto createdDto = new TypeOfTaskDto();
        createdDto.setId(1L);
        createdDto.setName("Test Type");

        String jwt = "token";

        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(typeService.createType(eq(boardId), any(TypeOfTaskDto.class))).thenReturn(createdDto);

        // Act & Assert
        mockMvc.perform(post("/board/{boardId}/type", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputDto))
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Type"));
    }

    @Test
    void moveTypes_ReturnsOk_WhenValidInput() throws Exception {
        // Arrange
        List<TypeOfTaskDto> updatedTypes = Collections.emptyList();
        doNothing().when(typeService).moveTypes(anyList());

        // Act & Assert
        mockMvc.perform(put("/board/types/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTypes))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void updateType_ReturnsUpdatedType_WhenValidInput() throws Exception {
        // Arrange
        Long typeId = 1L;
        TypeOfTaskDto inputDto = new TypeOfTaskDto();
        TypeOfTaskDto updatedDto = new TypeOfTaskDto();
        updatedDto.setId(typeId);
        updatedDto.setName("Updated Type");

        String jwt = "token";

        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(typeService.updateType(eq(typeId), any(TypeOfTaskDto.class))).thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(put("/board/type/{typeId}", typeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputDto))
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(typeId))
                .andExpect(jsonPath("$.name").value("Updated Type"));
    }

    @Test
    void deleteType_ReturnsNoContent_WhenValidTypeId() throws Exception {
        // Arrange
        Long typeId = 1L;
        String jwt = "token";

        doNothing().when(typeService).deleteType(typeId);
        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));

        // Act & Assert
        mockMvc.perform(delete("/board/type/{typeId}", typeId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
