package com.example.user_management.service;

import com.example.user_management.dto.UserRequestDto;
import com.example.user_management.dto.UserResponseDto;
import com.example.user_management.entity.User;
import com.example.user_management.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = new User(
                userRequestDto.getNome(),
                userRequestDto.getCognome(),
                userRequestDto.getEmail(),
                userRequestDto.getIndirizzo()
        );
        return toResponseDto(userRepository.save(user));
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return mapUsersToResponseDtos(users);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));
        return toResponseDto(user);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));

        user.setNome(userRequestDto.getNome());
        user.setCognome(userRequestDto.getCognome());
        user.setEmail(userRequestDto.getEmail());
        user.setIndirizzo(userRequestDto.getIndirizzo());

        return toResponseDto(userRepository.save(user));
    }

    public Map<String, Object> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato");
        }

        userRepository.deleteById(id);
        return Map.of("deleted", true);
    }

    public List<UserResponseDto> searchUsers(String nome, String cognome) {
        String normalizedNome = normalize(nome);
        String normalizedCognome = normalize(cognome);

        if (normalizedNome.isEmpty() && normalizedCognome.isEmpty()) {
            List<User> users = userRepository.findAll();
            return mapUsersToResponseDtos(users);
        }

        List<User> users = userRepository.findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCase(normalizedNome, normalizedCognome);
        return mapUsersToResponseDtos(users);
    }

    public Map<String, Object> uploadUsersFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File CSV vuoto");
        }

        if (!isCsvFile(file)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato file non valido. Caricare un CSV");
        }

        List<User> usersToSave = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(";", -1);
                if (columns.length < 4) {
                    continue;
                }

                String normalizedNome = normalize(columns[0]);
                String normalizedCognome = normalize(columns[1]);
                String email = normalize(columns[2]);
                String indirizzo = normalize(columns[3]);

                if ("nome".equalsIgnoreCase(normalizedNome)
                        && "cognome".equalsIgnoreCase(normalizedCognome)
                        && "email".equalsIgnoreCase(email)
                        && "indirizzo".equalsIgnoreCase(indirizzo)) {
                    continue;
                }

                usersToSave.add(new User(normalizedNome, normalizedCognome, email, indirizzo));
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante la lettura del file", e);
        }

        userRepository.saveAll(usersToSave);
        return Map.of("insertedUsers", usersToSave.size());
    }

    // Verifica se il file è un CSV
    private boolean isCsvFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
            return false;
        }
        return true;
    }

    // Normalizza una stringa rimuovendo spazi iniziali/finali e gestendo null
    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getNome(),
                user.getCognome(),
                user.getEmail(),
                user.getIndirizzo()
        );
    }

    private List<UserResponseDto> mapUsersToResponseDtos(List<User> users) {
        return users.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
