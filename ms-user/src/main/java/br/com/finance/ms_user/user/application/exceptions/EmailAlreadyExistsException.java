package br.com.finance.ms_user.user.application.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(){
        super("Email já cadastrado");
    }
}
