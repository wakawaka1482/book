package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.*;
import com.example.dto.CollectRecordDTO;
import com.example.dto.PaginationRequest;
import com.example.mapper.CollectRecordMapper;
import com.example.mapper.BookMapper;
import com.example.mapper.UserMapper;
import com.example.service.CollectRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectRecordServiceImpl extends ServiceImpl<CollectRecordMapper, CollectRecord> implements CollectRecordService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CollectRecordMapper collectRecordMapper;
    @Autowired
    private BookMapper bookMapper;

    @Override
    public IPage<CollectRecordDTO> findPageByUser(PaginationRequest paginationRequest) {

        Integer userId = userMapper.findUserIdByUsername(paginationRequest.getUsername());
        if (userId == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        LambdaQueryWrapper<CollectRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectRecord::getUserid, userId);

        if (paginationRequest.getBookname() != null) {
            LambdaQueryWrapper<Book> bookQueryWrapper = new LambdaQueryWrapper<>();
            bookQueryWrapper.like(Book::getName, paginationRequest.getBookname());
            List<Book> books = bookMapper.selectList(bookQueryWrapper);
            if (!books.isEmpty()) {
                List<Integer> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
                queryWrapper.in(CollectRecord::getBookid, bookIds);
            } else {
                queryWrapper.in(CollectRecord::getBookid, Collections.emptyList());
            }
        }

        IPage<CollectRecord> page = new Page<>(paginationRequest.getCurrentPage(), paginationRequest.getPageSize());
        IPage<CollectRecord> collectRecordPage = this.page(page, queryWrapper);

        List<CollectRecordDTO> records = collectRecordPage.getRecords().stream().map(record -> {
            Book book = bookMapper.selectById(record.getBookid());
            if (book == null) {
                System.out.println("Book not found for bookid: " + record.getBookid());
                return null;
            }
            String username = userMapper.findUsernameByUserId(record.getUserid());
            byte[] imageBytes = book.getImage();
            String bookImage = (imageBytes != null) ? Base64.getEncoder().encodeToString(imageBytes) : "path/to/placeholder/image.jpg";
            CollectRecordDTO dto = new CollectRecordDTO(record.getCollectrecordid(),"data:image/png;base64," + bookImage, book.getName(), username, record.getLenddate());
            return dto;
        }).collect(Collectors.toList());

        records = records.stream().filter(record -> record != null).collect(Collectors.toList());

        IPage<CollectRecordDTO> dtoPage = new Page<>();
        dtoPage.setCurrent(collectRecordPage.getCurrent());
        dtoPage.setSize(collectRecordPage.getSize());
        dtoPage.setTotal(collectRecordPage.getTotal());
        dtoPage.setRecords(records);

        return dtoPage;
    }

    @Override
    public IPage<CollectRecordDTO> findPageByAdmin(PaginationRequest paginationRequest) {

        LambdaQueryWrapper<CollectRecord> queryWrapper = new LambdaQueryWrapper<>();

        // 如果有查询条件，根据图书名称模糊查询
        if (paginationRequest.getBookname() != null) {
            LambdaQueryWrapper<Book> bookQueryWrapper = new LambdaQueryWrapper<>();
            bookQueryWrapper.like(Book::getName, paginationRequest.getBookname());
            List<Book> books = bookMapper.selectList(bookQueryWrapper);
            if (!books.isEmpty()) {
                List<Integer> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
                queryWrapper.in(CollectRecord::getBookid, bookIds);
            } else {
                queryWrapper.in(CollectRecord::getBookid, Collections.emptyList());
            }
        }

        IPage<CollectRecord> page = new Page<>(paginationRequest.getCurrentPage(), paginationRequest.getPageSize());
        IPage<CollectRecord> collectRecordPage = collectRecordMapper.selectPage(page, queryWrapper);

        List<CollectRecordDTO> records = collectRecordPage.getRecords().stream().map(record -> {
            Book book = bookMapper.selectById(record.getBookid());
            if (book == null) {
                System.out.println("Book not found for bookid: " + record.getBookid());
                return null;
            }
            String username = userMapper.findUsernameByUserId(record.getUserid());
            byte[] imageBytes = book.getImage();
            String bookImage = (imageBytes != null) ? Base64.getEncoder().encodeToString(imageBytes) : "path/to/placeholder/image.jpg";
            CollectRecordDTO dto = new CollectRecordDTO(record.getCollectrecordid(), "data:image/png;base64," + bookImage, book.getName(), username, record.getLenddate());
            return dto;
        }).collect(Collectors.toList());

        records = records.stream().filter(record -> record != null).collect(Collectors.toList());

        IPage<CollectRecordDTO> dtoPage = new Page<>();
        dtoPage.setCurrent(collectRecordPage.getCurrent());
        dtoPage.setSize(collectRecordPage.getSize());
        dtoPage.setTotal(collectRecordPage.getTotal());
        dtoPage.setRecords(records);

        return dtoPage;
    }

    @Override
    public boolean returnBook(Integer collectrecordid) {
        return this.removeById(collectrecordid);
    }

    @Override
    public boolean addCollectRecord(CollectRecord collectRecord) {
        return collectRecordMapper.insert(collectRecord) > 0;
    }

}
