package com.example.handler;

import com.example.dto.AuthRequest;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleAuthHandler implements AuthHandler {

    @Autowired
    private AuthService authService;

    private AuthHandler nextHandler;

    @Override
    public void setNextHandler(AuthHandler nextHandler) {

        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(AuthRequest request) {
        if (request.isAuthenticated()) {
            String role = authService.getRole(request.getUsername());
            request.setRole(role);
            if (nextHandler != null) {
                nextHandler.handle(request);
            }
        }
    }
}
