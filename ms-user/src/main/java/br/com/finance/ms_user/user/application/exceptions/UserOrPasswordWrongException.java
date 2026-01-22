package br.com.finance.ms_user.user.application.exceptions;

public class UserOrPasswordWrongException extends RuntimeException{
    public UserOrPasswordWrongException(){
        super("Usuário ou senha incorretos");
    }
}
