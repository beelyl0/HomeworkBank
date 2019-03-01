package com.homework.exceptions;

public class AccountToNotFoundException extends RuntimeException {

    public AccountToNotFoundException() {
        super();
    }

    public AccountToNotFoundException(String message) {
        super(message);
    }

}