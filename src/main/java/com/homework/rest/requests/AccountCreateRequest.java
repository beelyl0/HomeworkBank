package com.homework.rest.requests;

import java.math.BigDecimal;

public class AccountCreateRequest {

    private BigDecimal startAmount;

    public BigDecimal getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(BigDecimal startAmount) {
        this.startAmount = startAmount;
    }

}
