package com.example.user_management.repository;


import com.example.user_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCase(
            String nome,
            String cognome
    );
}
