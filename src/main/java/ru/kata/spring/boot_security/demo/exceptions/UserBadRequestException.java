package ru.kata.spring.boot_security.demo.exceptions;

public class UserBadRequestException extends RuntimeException {
    public UserBadRequestException(String message) {
        super(message);
    }
}
