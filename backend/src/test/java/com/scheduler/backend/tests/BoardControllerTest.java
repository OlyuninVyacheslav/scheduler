package com.scheduler.backend.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.backend.dtos.BoardDto;
import com.scheduler.backend.dtos.UserDto;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.UserRepository;
import com.scheduler.backend.services.BoardService;
import com.scheduler.backend.config.UserAuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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

    @MockBean
    private UserAuthenticationProvider userAuthenticationProvider;

    @Test
    public void testCreateBoard() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        BoardDto boardDto = new BoardDto(); // Initialize with appropriate values if needed
        boardDto.setName("Test Board");

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(userAuthenticationProvider.validateToken(Mockito.anyString())).thenReturn(authentication);

        String token = "Bearer valid.jwt.token";

        when(boardService.createBoard(Mockito.any(BoardDto.class), Mockito.any(User.class)))
                .thenReturn(boardDto);

        mockMvc.perform(post("/boards/create")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(boardDto)))
                .andExpect(status().isCreated());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}