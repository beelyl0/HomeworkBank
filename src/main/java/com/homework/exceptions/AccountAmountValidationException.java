package com.homework.exceptions;

public class AccountAmountValidationException extends RuntimeException {

    public AccountAmountValidationException() {
        super();
    }

    public AccountAmountValidationException(String message) {
        super(message);
    }

}
