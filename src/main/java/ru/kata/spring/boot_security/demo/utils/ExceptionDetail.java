package ru.kata.spring.boot_security.demo.utils;

public class ExceptionDetail {
    private String message;

    public ExceptionDetail(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
