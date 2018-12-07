package br.dev.valmirt.login.system.exception;

import java.util.Date;

public class ErrorResponse {
    private int status;
    private String message;
    private Date date;

    public ErrorResponse(){}

    public ErrorResponse(int status, String message, Date date) {
        this.status = status;
        this.message = message;
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
