package com.scheduler.backend.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.backend.controllers.AuthController;
import com.scheduler.backend.dtos.CredentialsDto;
import com.scheduler.backend.dtos.SignUpDto;
import com.scheduler.backend.dtos.UserDto;
import com.scheduler.backend.services.UserService;
import com.scheduler.backend.config.UserAuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserDto userDto;
    private CredentialsDto credentialsDto;
    private SignUpDto signUpDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        userDto = UserDto.builder()
                .id(1L)
                .firstname("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .token("dummyToken")
                .build();

        credentialsDto = CredentialsDto.builder()
                .email("john.doe@example.com")
                .password("password".toCharArray())
                .build();

        signUpDto = SignUpDto.builder()
                .firstname("John")
                .surname("Doe")
                .patronymic("M")
                .email("john.doe@example.com")
                .password("password".toCharArray())
                .build();
    }

    @Test
    void testLogin() throws Exception {
        Mockito.when(userService.login(any(CredentialsDto.class))).thenReturn(userDto);
        Mockito.when(userAuthenticationProvider.createToken(eq(userDto.getEmail()))).thenReturn("dummyToken");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.token").value("dummyToken"));
    }

    @Test
    void testRegister() throws Exception {
        Mockito.when(userService.register(any(SignUpDto.class))).thenReturn(userDto);
        Mockito.when(userAuthenticationProvider.createToken(eq(userDto.getEmail()))).thenReturn("dummyToken");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.token").value("dummyToken"));
    }
}
