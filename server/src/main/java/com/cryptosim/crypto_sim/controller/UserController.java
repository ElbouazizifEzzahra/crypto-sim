package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.dto.user.PasswordChangeDto;
import com.cryptosim.crypto_sim.dto.user.UserDtoRequest;
import com.cryptosim.crypto_sim.dto.user.UserDtoResponse;
import com.cryptosim.crypto_sim.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody PasswordChangeDto passwordChangeDto) {
        userService.changeUserPassword(id, passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword(), null);
        return ResponseEntity.ok().build();
    }
}
