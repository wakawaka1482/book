package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Integer id;
    private byte[] image;
    private String type;
    private String name;
    private String description;
    private transient MultipartFile imageFile;
}
