package org.chasapi.microservices.userservice.service;

import org.chasapi.microservices.userservice.dto.UserRequest;
import org.chasapi.microservices.userservice.dto.UserResponse;
import org.chasapi.microservices.userservice.model.User;
import org.chasapi.microservices.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(UserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password()); // Remember to encode this in production!
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    public UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
