package com.homework.rest.requests;

import java.math.BigDecimal;

public class TransferAmountRequest {

    private String toAccountNumber;

    private BigDecimal amount;

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
