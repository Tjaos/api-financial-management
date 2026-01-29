package br.com.finance.ms_user.user.domain.entities.user;

import br.com.finance.ms_user.user.domain.exceptions.EmailDoesNotBeNullOrEmptException;
import br.com.finance.ms_user.user.domain.exceptions.InvalidPasswordSizeException;
import br.com.finance.ms_user.user.domain.exceptions.NullOrBlankNameEmailOrPassordException;
import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private String email;
    private String password;


    public User(String name, String email, String password) {
        verifyInvalidValues(name, email, password);
        verifyPasswordSize(password);
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public User(UUID id, String name, String email, String password) {
        verifyInvalidValues(name, email, password);
        verifyPasswordSize(password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public void changeName(String name) {
        if(name == null || name.isBlank()){
            throw new NullOrBlankNameEmailOrPassordException();
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
            throw new NullOrBlankNameEmailOrPassordException();
        }

        verifyPasswordSize(password);
        this.password = password;
    }

    private static void verifyInvalidValues(String name, String email, String password) {
        if(name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new NullOrBlankNameEmailOrPassordException();
        }
    }

    private static void verifyPasswordSize(String password) {
        if(password.length() < 6) {
            throw new InvalidPasswordSizeException();
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
