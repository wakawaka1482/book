package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaginationRequest {
    private int currentPage;
    private int pageSize;
    private String bookname;
    private String username;
}
