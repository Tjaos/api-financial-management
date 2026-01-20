package br.com.finance.ms_user.User.domain.entities.user;

import java.time.LocalDate;
import java.util.UUID;

public class User {

    private UUID id;
    private String nome;
    private String email;
    private String password_hash;



    public UUID getId() {
        return id;
    }
    public String getPassword_hash() {
        return password_hash;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }


    public User(String nome, String email, String password_hash) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.password_hash = password_hash;
    }

    @Override
    public String toString() {
        return "Usuario: nome: %s, email: %s".formatted(nome, email);
    }
}
