package br.com.finance.ms_user.user.domain.exceptions;

public class NullOrBlankInputValuesException extends RuntimeException {
    public NullOrBlankInputValuesException(String message) {
        super(message);
    }

}
