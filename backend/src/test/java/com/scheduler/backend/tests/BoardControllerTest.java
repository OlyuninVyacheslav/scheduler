package com.scheduler.backend.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.controllers.BoardController;
import com.scheduler.backend.dtos.BoardDto;
import com.scheduler.backend.services.BoardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;

    @InjectMocks
    private BoardController boardController;

    private MockMvc mockMvc;

    @Test
    void getBoardById_ReturnsBoard_WhenValidId() throws Exception {
        // Arrange
        BoardDto boardDto = new BoardDto();
        boardDto.setId(1L);
        boardDto.setName("Test Board");
        when(boardService.getBoardById(anyLong())).thenReturn(boardDto);

        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();

        // Act & Assert
        mockMvc.perform(get("/boards/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Board"));
    }


    @Test
    void getAllBoards_ReturnsListOfBoards_WhenValidToken() throws Exception {
        // Arrange
        BoardDto boardDto = BoardDto.builder()
                .id(1L)
                .name("Test Board")
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .build();
        when(boardService.getAllBoardsForCurrentUser(anyLong())).thenReturn(Collections.singletonList(boardDto));

        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();

        // Act & Assert
        mockMvc.perform(get("/boards")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Board"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));
    }

    // Преобразование объекта в JSON строку для запроса
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}