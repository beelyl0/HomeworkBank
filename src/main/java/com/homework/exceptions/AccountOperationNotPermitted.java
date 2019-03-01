package com.homework.exceptions;

public class AccountOperationNotPermitted extends RuntimeException {

    public AccountOperationNotPermitted() {
        super();
    }

    public AccountOperationNotPermitted(String message) {
        super(message);
    }

}
