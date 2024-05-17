package com.scheduler.backend.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.backend.dtos.BoardDto;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.UserRepository;
import com.scheduler.backend.services.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BoardService boardService;

    @Test
    public void testCreateBoard() throws Exception {
        User mockUser = new User(); // Создаём заглушку пользователя
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        BoardDto boardDto = new BoardDto(); // Создаём объект BoardDto для создания доски

        mockMvc.perform(post("/boards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(boardDto))
                        .param("creatorId", "1")) // Передаём параметр creatorId
                .andExpect(status().isCreated()); // Проверяем, что доска успешно создана
    }

    // Преобразование объекта в JSON строку
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
