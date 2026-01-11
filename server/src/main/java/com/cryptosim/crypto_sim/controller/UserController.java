package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.dto.user.PasswordChangeDto;
import com.cryptosim.crypto_sim.dto.user.UserDtoRequest;
import com.cryptosim.crypto_sim.dto.user.UserDtoResponse;
import com.cryptosim.crypto_sim.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserDtoResponse> addUser(@Valid @RequestBody UserDtoRequest userRequestDTO) {
        UserDtoResponse userResponse = userService.addUser(userRequestDTO);
        if (userResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> editUser(@PathVariable Long id, @Valid @RequestBody UserDtoRequest userRequestDTO) {
        userRequestDTO.setId(id);
        userService.editUser(userRequestDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        List<UserDtoResponse> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody PasswordChangeDto passwordChangeDto,
            Authentication authentication) {

        String email = authentication.getName(); // email du user connecté

        userService.changeUserPassword(
                null,
                passwordChangeDto.getCurrentPassword(),
                passwordChangeDto.getNewPassword(),
                email
        );

        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserDtoResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        com.cryptosim.crypto_sim.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDtoResponse response = new UserDtoResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/me")
    public ResponseEntity<UserDtoResponse> updateCurrentUser(
            @Valid @RequestBody UserDtoRequest userRequestDTO,
            Authentication authentication) {
        String email = authentication.getName();
        com.cryptosim.crypto_sim.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRequestDTO.setId(user.getId());
        userRequestDTO.setPassword(null);
        userService.editUser(userRequestDTO);
        
        com.cryptosim.crypto_sim.model.User updatedUser = userService.getUserByEmail(email);
        UserDtoResponse response = new UserDtoResponse();
        response.setId(updatedUser.getId());
        response.setEmail(updatedUser.getEmail());
        response.setFirstName(updatedUser.getFirstName());
        response.setLastName(updatedUser.getLastName());
        return ResponseEntity.ok(response);
    }
    
}
