package com.example.controller;

import com.example.entity.Book;
import com.example.entity.LendRecord;
import com.example.dto.LendRecordDTO;
import com.example.mapper.BookMapper;
import com.example.service.IBookService;
import com.example.service.LendService;
import com.example.mapper.UserMapper;
import com.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lend")
public class LendController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LendService lendService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @PostMapping("/findPageUser")
    public R findPageUser(@RequestBody Map<String, Object> params) {
        try {
            String username = (String) params.get("username");
            int currentPage = (int) params.get("currentPage");
            int pageSize = (int) params.get("pageSize");

            // 通过用户名查询用户ID
            Integer userid = userMapper.findUserIdByUsername(username);
            if (userid == null) {
                return new R(false, "用户不存在");
            }

            // 查询用户的借阅记录
            int offset = (currentPage - 1) * pageSize;
            List<LendRecord> records = lendService.findLendRecordsByUserId(userid, pageSize, offset);
            int total = lendService.countLendRecordsByUserId(userid);

            List<LendRecordDTO> recordDTOs = new ArrayList<>();
            for (LendRecord record : records) {
                // 查询bookname和username
                String bookname = bookMapper.findBookNameById(record.getBookid());
                String lendUsername = userMapper.findUsernameById(record.getUserid());

                // 设置backtime为lendtime的一个月以后
                LocalDateTime backtime = record.getLendtime().plusMonths(1);

                // 构建LendRecordDTO
                LendRecordDTO recordDTO = new LendRecordDTO(record.getLendid(),bookname, lendUsername, record.getLendtime(), backtime);
                recordDTOs.add(recordDTO);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("records", recordDTOs);


            System.out.println("返回的结果: ");
            System.out.println(result);

            return new R(true, result);
        } catch (Exception e) {
            /*e.printStackTrace(); // 打印异常堆栈信息以便调试*/
            return new R(false, "获取借阅记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/findPageAll")
    public R findPageAll(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = (int) params.get("currentPage");
            int pageSize = (int) params.get("pageSize");
            String queryString = (String) params.get("queryString");

            // 计算分页偏移量
            int offset = (currentPage - 1) * pageSize;

            // 分页查询所有借阅记录
            List<LendRecord> records;
            int total;
            if (queryString != null && !queryString.isEmpty()) {
                // 如果有查询条件，根据图书编号查询
                records = lendService.findLendRecordsByBookId(queryString, pageSize, offset);
                total = lendService.countLendRecordsByBookId(queryString);
            } else {
                // 如果没有查询条件，查询所有借阅记录
                records = lendService.findAllLendRecords(pageSize, offset);
                total = lendService.countAllLendRecords();
            }

            List<LendRecordDTO> recordDTOs = new ArrayList<>();
            for (LendRecord record : records) {
                // 查询bookname和username
                String bookname = bookMapper.findBookNameById(record.getBookid());
                String lendUsername = userMapper.findUsernameById(record.getUserid());

                // 设置backtime为lendtime的一个月以后
                LocalDateTime backtime = record.getLendtime().plusMonths(1);

                // 构建LendRecordDTO
                LendRecordDTO recordDTO = new LendRecordDTO(record.getLendid(), bookname, lendUsername, record.getLendtime(), backtime);
                recordDTOs.add(recordDTO);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("rows", recordDTOs);

            return new R(true, result);
        } catch (Exception e) {
            return new R(false, "获取借阅记录失败: " + e.getMessage());
        }
    }


    @PostMapping("/add")
    public R addLendRecord(@RequestBody LendRecord lendRecord) {
        try {
            Book book = bookService.getById(lendRecord.getBookid());
            book.setNumber(book.getNumber() - 1);
            boolean updateSuccess = bookService.updateById(book);
            if (updateSuccess) {
                lendService.addLendRecord(lendRecord);
                return new R(true, "借阅记录添加成功");
            } else {
                return new R(false, "借阅记录添加失败");
            }
        } catch (Exception e) {
            return new R(false, "添加借阅记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/return")
    public R returnBook(@RequestBody Map<String, Object> params) {
        try {
            Integer lendid = (Integer) params.get("lendid");
            Integer bookid = lendService.getBookIdByLendId(lendid);
            if (lendid == null) {
                return new R(false, "借阅记录ID不能为空");
            }

            // 更新归还时间
            lendService.updateBackTime(lendid, LocalDateTime.now());
            // 获取书籍并更新数量
            Book book = bookService.getById(bookid);
            if (book == null) {
                return new R(false, "未找到对应的图书");
            }

            book.setNumber(book.getNumber() + 1);
            bookService.updateById(book);  // 更新书籍信息

            return new R(true, "归还图书成功");

        } catch (Exception e) {
            return new R(false, "归还图书失败: " + e.getMessage());
        }
    }

    @GetMapping("/delete")
    public R deleteLend(@RequestParam("lendid") Integer lendid) {
        try {
            boolean isDeleted = lendService.deleteLend(lendid);
            if (isDeleted) {
                return new R(true, "删除成功");
            } else {
                return new R(false, "删除失败，记录不存在");
            }
        } catch (Exception e) {
            return new R(false, "删除失败: " + e.getMessage());
        }
    }
}
