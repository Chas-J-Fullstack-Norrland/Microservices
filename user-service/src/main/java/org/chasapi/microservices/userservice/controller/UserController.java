package org.chasapi.microservices.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.chasapi.microservices.userservice.service.UserService;
import org.chasapi.microservices.userservice.dto.UserRequest;
import org.chasapi.microservices.userservice.dto.UserResponse;
import org.chasapi.microservices.userservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints för hantering av användardata")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Hämtar alla användare
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Hämta användare med id
    @GetMapping("/{id}")
    @Operation(summary = "Hämta användare", description = "Returnerar en specifik användare via dess ID. Lösenord exponeras inte i dokumentationen baserat på tidigare krav.")
    @ApiResponse(responseCode = "200", description = "Användare hittades",
            content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "401", description = "Obehörig åtkomst (Saknad eller ogiltig token)")
    @ApiResponse(responseCode = "404", description = "Användaren existerar inte")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(userService::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Skapar användare konto
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Skapa användare", description = "Registrerar en ny användare i systemet.")
    @ApiResponse(responseCode = "201", description = "Användare skapad")
    @ApiResponse(responseCode = "400", description = "Ogiltig inmatning")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUser(request));
    }

    // Tar bort användaren och returnerar inget
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
