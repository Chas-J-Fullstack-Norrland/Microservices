package org.chasapi.microservices.userservice;

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
public class UserRepositoryTest {


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user-service-test")
            .withUsername("admin")
            .withPassword("password");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void save_shouldPersistUser() {
        User user = new User(null, "alice", "pass", "ROLE_USER");
        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("alice");
    }

    @Test
    void findAll_shouldReturnAllPersistedUsers() {
        userRepository.save(new User(null, "alice", "pass", "ROLE_USER"));
        userRepository.save(new User(null, "bob",   "pass", "ROLE_ADMIN"));

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    void findById_whenExists_shouldReturnUser() {
        User saved = userRepository.save(new User(null, "alice", "pass", "ROLE_USER"));

        Optional<User> result = userRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        Optional<User> result = userRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByUsername_whenExists_shouldReturnUser() {
        userRepository.save(new User(null, "alice", "pass", "ROLE_USER"));

        Optional<User> result = userRepository.findByUsername("alice");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void findByUsername_whenNotExists_shouldReturnEmpty() {
        Optional<User> result = userRepository.findByUsername("nobody");

        assertThat(result).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveUser() {
        User saved = userRepository.save(new User(null, "alice", "pass", "ROLE_USER"));

        userRepository.deleteById(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void save_duplicateUsername_shouldThrowException() {
        userRepository.save(new User(null, "alice", "pass1", "ROLE_USER"));

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(new User(null, "alice", "pass2", "ROLE_USER"));
        });
    }
}



