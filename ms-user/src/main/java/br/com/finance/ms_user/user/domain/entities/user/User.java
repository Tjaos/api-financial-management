package br.com.finance.ms_user.user.domain.entities.user;

import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private String email;
    private String password;


    public User(String name, String email, String password) {
        this(UUID.randomUUID(), name, email, password);
    }
    // Rehidratação (infra → domain)
    public User(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void changeName(String name) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.name = name;
    }

    public void changeEmail(String email) {
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        this.email = email;
    }

    public void changePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Senha inválida");
        }
        this.password = password;
    }


    public UUID getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }



    @Override
    public String toString() {
        return "Usuario: nome: %s, email: %s".formatted(name, email);
    }
}
