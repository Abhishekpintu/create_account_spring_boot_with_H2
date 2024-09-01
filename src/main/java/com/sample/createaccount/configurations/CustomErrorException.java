package com.sample.createaccount.configurations;

public class CustomErrorException extends RuntimeException {

    public CustomErrorException(String message) {
        super(message);
    }

}
