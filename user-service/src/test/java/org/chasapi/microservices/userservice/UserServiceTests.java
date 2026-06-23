package org.chasapi.microservices.userservice;


import org.chasapi.microservices.userservice.Service.UserService;
import org.chasapi.microservices.userservice.model.User;
import org.chasapi.microservices.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserServiceTests {

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
    UserRepository repository;

    @Autowired
    UserService userService;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    // --- UserService tests ---

    @Test
    void registerUser_shouldSetRoleToRoleUser_andPersist() {
        User input = new User(null, "john", "secret", null);

        User saved = userService.registerUser(input);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRole()).isEqualTo("ROLE_USER");
        assertThat(saved.getUsername()).isEqualTo("john");
    }

    @Test
    void getAllUsers_shouldReturnAllPersistedUsers() {
        userService.registerUser(new User(null, "alice", "pass", null));
        userService.registerUser(new User(null, "bob",   "pass", null));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
    }

    @Test
    void getUserById_whenExists_shouldReturnUser() {
        User saved = userService.registerUser(new User(null, "alice", "pass", null));

        Optional<User> result = userService.getUserById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void getUserById_whenNotExists_shouldReturnEmpty() {
        Optional<User> result = userService.getUserById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void deleteUser_shouldRemoveUserFromDb() {
        User saved = userService.registerUser(new User(null, "alice", "pass", null));

        userService.deleteUser(saved.getId());

        assertThat(userService.getUserById(saved.getId())).isEmpty();
    }

}
