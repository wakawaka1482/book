package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Book;
import com.example.dto.BookTypeCount;
import com.example.service.IBookService;
import com.example.utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/books")
public class BookControllerPro {
    @Resource
    private IBookService iBookService;

    @GetMapping
    public R getAll() {
        List<Book> bookList = iBookService.list();
        return new R(true ,bookList);
    }


    @PostMapping
    public R save(@RequestParam("book") String bookJson, @RequestParam("image") MultipartFile image) throws IOException {
        // 将JSON字符串转换为Book对象
        Book book = new ObjectMapper().readValue(bookJson, Book.class);

        // 处理上传的照片数据
        if (!image.isEmpty()) {
            book.setImage(image.getBytes());
        }

        Boolean flag = iBookService.save(book);
        return new R(flag, flag ? "添加成功" : "添加失败");
    }

    @PutMapping
    public R update(@RequestParam("book") String bookJson, @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        // 将JSON字符串转换为Book对象
        Book book = new ObjectMapper().readValue(bookJson, Book.class);

        // 处理上传的照片数据
        if (image != null && !image.isEmpty()) {
            book.setImage(image.getBytes());
        }

        Boolean flag = iBookService.updateById(book);
        return new R(flag, flag ? "更新成功" : "更新失败");
    }


    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id){
        Boolean flag = iBookService.removeById(id);
        return new R(flag, flag ? "删除成功" : "删除失败");
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id){
        Book book = iBookService.getById(id);
        return new R(true,book);
    }

    @GetMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {
        IPage<Book> pageBook = iBookService.getPage(currentPage,pageSize,book);
        return new R(null != pageBook ,pageBook);
    }
    // 新增搜索图书接口
    @GetMapping("/search")
    public R searchBooks(@RequestParam String name, @RequestParam String type, @RequestParam String description) {
        List<Book> books = iBookService.searchBooks(name, type, description);
        return new R(true, books);
    }

    @PostMapping("/EcharsShow")
    public List<BookTypeCount> getBookTypeCountsUser() {
        return iBookService.getBookTypeCounts();
    }

    @PostMapping("/EcharsShowAdmin")
    public R getBookTypeCountsAdmin() {
        Map<String, Object> result = new HashMap<>();
        List<BookTypeCount> bookTypeCounts = iBookService.getBookTypeCounts();
        List<String> setmealNames = bookTypeCounts.stream()
                .map(BookTypeCount::getType)
                .collect(Collectors.toList());
        result.put("bookTypeCounts", bookTypeCounts); // 原始数据
        result.put("setmealNames", setmealNames); // 提取后的书籍类型名称数组

        return new R(true,result);
    }
}
