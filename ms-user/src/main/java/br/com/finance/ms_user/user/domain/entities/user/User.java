package br.com.finance.ms_user.user.domain.entities.user;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String name;
    private final String email;
    private final String password;


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
    public boolean passwordMatches(String password) {
        return this.password.equals(password);
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
