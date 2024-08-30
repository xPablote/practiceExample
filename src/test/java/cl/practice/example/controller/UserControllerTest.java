package cl.practice.example.controller;

import cl.practice.example.dto.UserDto;
import cl.practice.example.entity.User;
import cl.practice.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCreateUserOK() throws Exception {
        UserDto userDto = new UserDto();
        User user = new User();

        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Usuario creado con éxito"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    public void testCreateUserConflict() throws Exception {
        UserDto userDto = new UserDto();

        when(userService.createUser(any(UserDto.class)))
                .thenThrow(new IllegalArgumentException("Creacion de usuario sin exito"));

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Creacion de usuario sin exito"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void TestUserByEmailOK() throws Exception {
        // Given
        String email = "practice@example.cl";
        User user = new User();
        user.setEmail(email);
        user.setName("Practice Example");

        when(userService.getUserByEmail(email)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/users/byEmail")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario encontrado por email: "))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.name").value("Practice Example"));

        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void TestUserByEmailNotFound() throws Exception {
        // Given
        String email = "noencontrado@example.cl";

        when(userService.getUserByEmail(email)).thenThrow(new RuntimeException("Email inexistente"));

        // When & Then
        mockMvc.perform(get("/users/byEmail")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Email inexistente"));

        verify(userService, times(1)).getUserByEmail(email);
    }

}