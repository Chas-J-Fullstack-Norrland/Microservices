package org.chasapi.microservices.userservice;

import org.chasapi.microservices.userservice.model.User;
import org.chasapi.microservices.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class UserControllerTests {

    @Container
    static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user-service")
            .withUsername("admin")
            .withPassword("password");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    // ── GET /api/users ──────────────────────────────────────────────────────────

    @Test
    void getAllUsers_whenEmpty_shouldReturn200WithEmptyList() throws Exception {
        mockMvc.perform(get("/api/users").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllUsers_shouldReturn200WithAllUsers() throws Exception {
        userRepository.save(new User(null, "alice", "pass", "ROLE_USER"));
        userRepository.save(new User(null, "bob",   "pass", "ROLE_ADMIN"));

        mockMvc.perform(get("/api/users").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("alice", "bob")));
    }

    // ── GET /api/users/{id} ─────────────────────────────────────────────────────

    @Test
    void getUserById_whenExists_shouldReturn200() throws Exception {
        User saved = userRepository.save(new User(null, "alice", "pass", "ROLE_USER"));

        mockMvc.perform(get("/api/users/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void getUserById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/users/999").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isNotFound());
    }

    // ── POST /api/users/createUser ────────────────────────────────────────────────

    @Test
    void createUser_shouldReturn201AndPersistUser() throws Exception {
        String body = """
            {
                "username": "john",
                "password": "secretpassword"
            }
            """;

        mockMvc.perform(post("/api/users/register")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Verify password is NOT leaked
    }

    // ── DELETE /api/users/{id} ──────────────────────────────────────────────────

    @Test
    void deleteUser_shouldReturn204AndRemoveFromDb() throws Exception {
        User saved = userRepository.save(new User(null, "alice", "pass", "ROLE_USER"));

        mockMvc.perform(delete("/api/users/" + saved.getId()))
                .andExpect(status().isNoContent());

        assert userRepository.findById(saved.getId()).isEmpty();
    }

    @Test
    void deleteUser_nonExistentId_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/users/999").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserById_shouldReturnResponseDto() throws Exception {
        // Manually save a user to the repository
        User user = userRepository.save(new User(null, "alice", "encoded_pass", "ROLE_USER"));

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Ensure DTO is used
    }

}