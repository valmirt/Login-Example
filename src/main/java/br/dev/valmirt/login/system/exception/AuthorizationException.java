package br.dev.valmirt.login.system.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(){}

    public AuthorizationException(String message){
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
