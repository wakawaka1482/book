package com.example.handler;

import com.example.dto.AuthRequest;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthHandler implements AuthHandler {

    @Autowired
    private AuthService authService;

    private AuthHandler nextHandler;

    @Override
    public void setNextHandler(AuthHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(AuthRequest request) {
        boolean isAuthenticated = authService.authenticate(request.getUsername(), request.getPassword());
        if (isAuthenticated) {
            request.setAuthenticated(true);
            if (nextHandler != null) {
                nextHandler.handle(request);
            }
        } else {
            request.setAuthenticated(false);
        }
    }
}

