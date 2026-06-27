package org.chasapi.microservices.userservice;


import org.chasapi.microservices.userservice.service.UserService;
import org.chasapi.microservices.userservice.dto.UserRequest;
import org.chasapi.microservices.userservice.dto.UserResponse;
import org.chasapi.microservices.userservice.model.User;
import org.chasapi.microservices.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.jpa.hibernate.ddl-auto=update"
})
@Testcontainers
@ActiveProfiles("test")
public class UserServiceTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user-service-test")
            .withUsername("admin")
            .withPassword("password");

    @Autowired
    UserRepository repository;

    @Autowired
    UserService userService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    // --- UserService tests ---

    @Test
    void registerUser_shouldSetRoleToRoleUser_andPersist() {
        // Create request DTO
        UserRequest request = new UserRequest("john", "secret123");

        // Service returns UserResponse
        UserResponse saved = userService.registerUser(request);

        assertThat(saved.id()).isNotNull();
        assertThat(saved.role()).isEqualTo("ROLE_USER");
        assertThat(saved.username()).isEqualTo("john");

        // Verify in DB
        assertThat(repository.existsById(saved.id())).isTrue();
    }

    @Test
    void getAllUsers_shouldReturnAllPersistedUsers() {
        userService.registerUser(new UserRequest("alice", "pass1234"));
        userService.registerUser(new UserRequest("bob", "pass1234"));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
    }

    @Test
    void getUserById_whenExists_shouldReturnUser() {
        UserResponse saved = userService.registerUser(new UserRequest("alice", "pass1234"));

        Optional<User> result = userService.getUserById(saved.id());

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void deleteUser_shouldRemoveUserFromDb() {
        UserResponse saved = userService.registerUser(new UserRequest("alice", "pass1234"));

        userService.deleteUser(saved.id());

        assertThat(userService.getUserById(saved.id())).isEmpty();
    }
}
