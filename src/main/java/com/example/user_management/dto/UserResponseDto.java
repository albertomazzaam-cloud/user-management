package com.example.user_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;
}
