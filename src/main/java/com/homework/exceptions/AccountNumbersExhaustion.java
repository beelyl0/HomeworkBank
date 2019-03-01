package com.homework.exceptions;

public class AccountNumbersExhaustion extends RuntimeException {

    public  AccountNumbersExhaustion() {
        super();
    }

    public  AccountNumbersExhaustion(String message) {
        super(message);
    }

}
