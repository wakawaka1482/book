package com.example.handler;

import com.example.dto.AuthRequest;

public interface AuthHandler {
    void setNextHandler(AuthHandler nextHandler);
    void handle(AuthRequest request);
}
