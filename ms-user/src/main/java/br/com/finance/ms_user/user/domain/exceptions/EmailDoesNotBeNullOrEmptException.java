package br.com.finance.ms_user.user.domain.exceptions;

public class EmailDoesNotBeNullOrEmptException extends RuntimeException {
    public EmailDoesNotBeNullOrEmptException() {
        super("O email não pode ser nulo ou vazio.");
    }
}
