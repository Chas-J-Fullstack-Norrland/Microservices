package org.chasapi.microservices.userservice;

import org.chasapi.microservices.userservice.controller.UserController;
import org.chasapi.microservices.userservice.dto.UserRequest;
import org.chasapi.microservices.userservice.dto.UserResponse;
import org.chasapi.microservices.userservice.model.User;
import org.chasapi.microservices.userservice.repository.UserRepository;
import org.chasapi.microservices.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class UserControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user-service-test")
            .withUsername("admin")
            .withPassword("password");

    // ── GET /api/v1/users ───────────────────────────────────────────────────────

    @Test
    void getAllUsers_whenEmpty_shouldReturn200WithEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllUsers_shouldReturn200WithAllUsers() throws Exception {
        // Förbereder fysisk data i databasen
        User user1 = new User();
        user1.setUsername("alice");
        user1.setPassword("pass");
        user1.setRole("ROLE_USER");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("bob");
        user2.setPassword("pass");
        user2.setRole("ROLE_ADMIN");
        userRepository.save(user2);

        mockMvc.perform(get("/api/v1/users").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("alice", "bob")));
    }

    // ── GET /api/v1/users/{id} ──────────────────────────────────────────────────

    @Test
    void getUserById_whenExists_shouldReturn200() throws Exception {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pass");
        user.setRole("ROLE_USER");
        User savedUser = userRepository.save(user);
        Long generatedId = savedUser.getId();

        mockMvc.perform(get("/api/v1/users/" + generatedId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getUserById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/users/9999").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ── POST /api/v1/users ──────────────────────────────────────────────────────

    @Test
    void createUser_shouldReturn201AndPersistUser() throws Exception {
        String body = """
            {
                "username": "john",
                "password": "secretpassword"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.password").doesNotExist());

        Optional<User> savedUser = userRepository.findByUsername("john");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getRole()).isEqualTo("ROLE_USER");
    }

    // ── DELETE /api/v1/users/{id} ───────────────────────────────────────────────

    @Test
    void deleteUser_shouldReturn204AndRemoveFromDb() throws Exception {
        User user = new User();
        user.setUsername("charlie");
        user.setPassword("pass");
        user.setRole("ROLE_USER");
        User savedUser = userRepository.save(user);
        Long generatedId = savedUser.getId();

        mockMvc.perform(delete("/api/v1/users/" + generatedId).with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(generatedId)).isEmpty();
    }

    @Test
    void deleteUser_nonExistentId_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/users/9999").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isNoContent());
    }
}

