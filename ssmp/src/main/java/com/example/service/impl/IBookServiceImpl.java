package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.Book;
import com.example.dto.BookTypeCount;
import com.example.mapper.BookMapper;
import com.example.service.IBookService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class IBookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Resource
    private BookMapper bookMapper;


    public IPage<Book> getPage(Integer currentPage,Integer pageSize,Book queryBook){
        IPage page = new Page(currentPage,pageSize);
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<Book>();
        lqw.like(Strings.isNotEmpty(queryBook.getName()),Book::getName,queryBook.getName());
        lqw.like(Strings.isNotEmpty(queryBook.getType()),Book::getType,queryBook.getType());
        lqw.like(Strings.isNotEmpty(queryBook.getDescription()), Book::getDescription,queryBook.getDescription());
        return bookMapper.selectPage(page,lqw);
    }


    @Override
    public List<Book> searchBooks(String name, String type, String description) {
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(name), Book::getName, name);
        lqw.like(Strings.isNotEmpty(type), Book::getType, type);
        lqw.like(Strings.isNotEmpty(description), Book::getDescription, description);
        return bookMapper.selectList(lqw);
    }


    @Override
    public List<BookTypeCount> getBookTypeCounts() {
        return bookMapper.countBooksByType();
    }
}
