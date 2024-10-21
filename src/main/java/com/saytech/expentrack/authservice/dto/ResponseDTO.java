package com.saytech.expentrack.authservice.dto;

public class ResponseDTO {

    String username;

    String message;

    String token;

    public ResponseDTO (String username, String message, String token) {
        this.username = username;
        this.message = message;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
