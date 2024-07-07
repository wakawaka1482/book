package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Book;
import com.example.dto.BookTypeCount;

import java.util.List;


public interface IBookService extends IService<Book> {
    IPage<Book> getPage(Integer currentPage,Integer pageSize,Book queryBook);

    List<Book> searchBooks(String name, String type, String description);

    List<BookTypeCount> getBookTypeCounts();

    boolean updateById(Book book);
}
