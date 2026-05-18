package com.example.user_management.controller;

import com.example.user_management.dto.UserRequestDto;
import com.example.user_management.dto.UserResponseDto;
import com.example.user_management.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint CRUD per la gestione utenti
    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(id, userRequestDto);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
    

    // Ricerca utenti con filtri opzionali su nome e cognome
    @GetMapping("/search")
    public List<UserResponseDto> searchUsers(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome
    ) {
        return userService.searchUsers(nome, cognome);
    }

    // Import utenti da file CSV
    @PostMapping("/upload-csv")
    public Map<String, Object> uploadUsersFromCsv(@RequestParam("file") MultipartFile file) {
        return userService.uploadUsersFromCsv(file);
    }
}

