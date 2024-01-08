package com.example.paymentcardvalidator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticateResponse {

    private String message;

    private String token;

    public AuthenticateResponse() {
    }

    public AuthenticateResponse(String message) {
        this.message = message;
    }

    public AuthenticateResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
