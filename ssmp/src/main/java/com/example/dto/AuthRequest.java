package com.example.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest {
    private String username;
    private String password;
    private boolean authenticated = false;
    private String role;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
