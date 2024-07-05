package com.example.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String phone;
    private String sex;
    private String password;
    private String role;
}
