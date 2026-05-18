package com.example.user_management.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;
}
