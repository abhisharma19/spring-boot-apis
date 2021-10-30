package com.change.management.apichangemanagement.domain.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class Error implements Serializable {

    private HttpStatus code;
    private String message;

    public Error() {

    }

    public Error(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
