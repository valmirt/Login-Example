package br.dev.valmirt.login.system.exception;

public class DataException extends RuntimeException {
    public DataException(){}

    public DataException(String message){
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
