package org.chasapi.microservices.userservice.Service;

import org.chasapi.microservices.userservice.model.User;
import org.chasapi.microservices.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    public User registerUser(User user) {
        user.setPassword(user.getPassword());
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByUserName(String name) {
        return userRepository.findByUsername(name);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
