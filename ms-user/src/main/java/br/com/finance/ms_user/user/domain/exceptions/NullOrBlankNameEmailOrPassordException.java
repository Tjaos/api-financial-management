package br.com.finance.ms_user.user.domain.exceptions;

public class NullOrBlankNameEmailOrPassordException extends RuntimeException {
    public NullOrBlankNameEmailOrPassordException() {
        super("Nome, email ou senha não podem ser nulos ou vazios");
    }

}
