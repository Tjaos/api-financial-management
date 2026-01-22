package br.com.finance.ms_user.user.domain.entities.user;

import br.com.finance.ms_user.user.domain.exceptions.EmailDoesNotBeNullOrEmptException;
import br.com.finance.ms_user.user.domain.exceptions.NullOrBlankInputValuesException;
import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private String email;
    private String password;


    public User(String name, String email, String password) {
        verifyInvalidValues(name, email, password);
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public User(UUID id, String name, String email, String password) {
        verifyInvalidValues(name, email, password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public void changeName(String name) {
        if(name == null || name.isBlank()){
            throw new NullOrBlankInputValuesException("Nome não pode ser nulo ou vazio");
        }
        this.name = name;
    }

    public void changeEmail(String email) {
        if(email == null || email.isBlank()){
            throw new EmailDoesNotBeNullOrEmptException();
        }
        this.email = email;
    }

    public void changePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new NullOrBlankInputValuesException("Senha não pode ser nula ou vazia");
        }
        this.password = password;
    }

    private static void verifyInvalidValues(String name, String email, String password) {
        if(name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()){
            throw new NullOrBlankInputValuesException("Nome, email e senha não podem ser nulos ou vazios");
        }
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
