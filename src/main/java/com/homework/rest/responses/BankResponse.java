package com.homework.rest.responses;

public class BankResponse<T> {

    private Status status;

    private T data;

    public BankResponse() {
    }

    public BankResponse(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public enum Status {
        SUCCESS,
        ERROR
    }

}
