package com.example.config;

import com.example.handler.AuthHandler;
import com.example.handler.RoleAuthHandler;
import com.example.handler.UsernamePasswordAuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthChainConfig {

    @Bean
    public AuthHandler authChain(UsernamePasswordAuthHandler usernamePasswordAuthHandler, RoleAuthHandler roleAuthHandler) {
        usernamePasswordAuthHandler.setNextHandler(roleAuthHandler);
        return usernamePasswordAuthHandler;
    }
}
