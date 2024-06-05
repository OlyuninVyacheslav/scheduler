package com.scheduler.backend.tests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.controllers.BoardController;
import com.scheduler.backend.dtos.BoardDto;
import com.scheduler.backend.dtos.UserDto;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.UserRepository;
import com.scheduler.backend.services.BoardService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BoardController boardController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    void getBoardById_ReturnsBoard_WhenValidId() throws Exception {
        // Arrange
        Long boardId = 1L;
        BoardDto boardDto = new BoardDto();
        when(boardService.getBoardById(boardId)).thenReturn(boardDto);

        // Act & Assert
        mockMvc.perform(get("/boards/{id}", boardId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createBoard_ReturnsCreatedBoard_WhenValidInput() throws Exception {
        // Arrange
        BoardDto inputDto = new BoardDto();
        BoardDto createdDto = new BoardDto();
        createdDto.setId(1L);

        String jwt = "token";
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        User user = new User();

        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(((Authentication) userAuthenticationProvider.validateToken(anyString())).getPrincipal()).thenReturn(userDto);
        when(userRepository.findById(userDto.getId())).thenReturn(java.util.Optional.of(user));
        when(boardService.createBoard(any(BoardDto.class), any(User.class))).thenReturn(createdDto);

        // Act & Assert
        mockMvc.perform(post("/boards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputDto))
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createBoard_ReturnsNotFound_WhenUserNotFound() throws Exception {
        // Arrange
        BoardDto inputDto = new BoardDto();

        String jwt = "some.valid.token";
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(((Authentication) userAuthenticationProvider.validateToken(anyString())).getPrincipal()).thenReturn(userDto);
        when(userRepository.findById(userDto.getId())).thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        mockMvc.perform(post("/boards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputDto))
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBoards_ReturnsBoards_WhenValidToken() throws Exception {
        // Arrange
        String jwt = "token";
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        List<BoardDto> boards = Collections.emptyList();

        when(userAuthenticationProvider.validateToken(anyString())).thenReturn(mock(Authentication.class));
        when(((Authentication) userAuthenticationProvider.validateToken(anyString())).getPrincipal()).thenReturn(userDto);
        when(boardService.getAllBoardsForCurrentUser(userDto.getId())).thenReturn(boards);

        // Act & Assert
        mockMvc.perform(get("/boards")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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