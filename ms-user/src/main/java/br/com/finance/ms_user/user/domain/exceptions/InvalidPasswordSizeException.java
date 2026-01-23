package br.com.finance.ms_user.user.domain.exceptions;

public class InvalidPasswordSizeException extends RuntimeException {
    public InvalidPasswordSizeException() {
        super("A senha deve ter no mínimo 6 caracteres");
    }
}
