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
import java.time.ZoneId;
import java.util.*;

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

    // 构建用户的 LendRecordDTO 列表，显示 backtime 为一个月后
    private List<LendRecordDTO> buildUserLendRecordDTOList(List<LendRecord> records) {
        List<LendRecordDTO> recordDTOs = new ArrayList<>();
        for (LendRecord record : records) {
            String bookname = bookMapper.findBookNameById(record.getBookid());
            String lendUsername = userMapper.findUsernameById(record.getUserid());
            LocalDateTime backtime = record.getLendtime().plusMonths(1);
            LendRecordDTO recordDTO = new LendRecordDTO(record.getLendid(), bookname, lendUsername, record.getLendtime(), backtime);
            recordDTOs.add(recordDTO);
        }
        return recordDTOs;
    }

    // 构建管理员的 LendRecordDTO 列表，显示数据库中的 backtime
    private List<LendRecordDTO> buildAdminLendRecordDTOList(List<LendRecord> records) {
        List<LendRecordDTO> recordDTOs = new ArrayList<>();
        for (LendRecord record : records) {
            String bookname = bookMapper.findBookNameById(record.getBookid());
            String lendUsername = userMapper.findUsernameById(record.getUserid());
            LendRecordDTO recordDTO = new LendRecordDTO(record.getLendid(), bookname, lendUsername, record.getLendtime(), record.getBacktime());
            recordDTOs.add(recordDTO);
        }
        return recordDTOs;
    }

    // 分页查询借阅记录
    private R queryLendRecords(String username, int currentPage, int pageSize, String queryString, boolean isAdmin) {
        try {
            int offset = (currentPage - 1) * pageSize;
            Integer userid = username != null ? userMapper.findUserIdByUsername(username) : null;

            if (username != null && userid == null) {
                return new R(false, "用户不存在");
            }

            List<LendRecord> records;
            int total;
            if (queryString != null && !queryString.isEmpty()) {
                records = lendService.findLendRecordsByBookId(queryString, pageSize, offset);
                total = lendService.countLendRecordsByBookId(queryString);
            } else if (userid != null) {
                records = lendService.findLendRecordsByUserId(userid, pageSize, offset);
                total = lendService.countLendRecordsByUserId(userid);
            } else {
                records = lendService.findAllLendRecords(pageSize, offset);
                total = lendService.countAllLendRecords();
            }

            List<LendRecordDTO> recordDTOs;
            if (isAdmin) {
                recordDTOs = buildAdminLendRecordDTOList(records);
            } else {
                recordDTOs = buildUserLendRecordDTOList(records);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("rows", recordDTOs);

            return new R(true, result);
        } catch (Exception e) {
            return new R(false, "获取借阅记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/findPageUser")
    public R findPageUser(@RequestBody Map<String, Object> params) {
        String username = (String) params.get("username");
        int currentPage = (int) params.get("currentPage");
        int pageSize = (int) params.get("pageSize");
        return queryLendRecords(username, currentPage, pageSize, null, false);
    }

    @PostMapping("/findPageAll")
    public R findPageAll(@RequestBody Map<String, Object> params) {
        int currentPage = (int) params.get("currentPage");
        int pageSize = (int) params.get("pageSize");
        String queryString = (String) params.get("queryString");
        return queryLendRecords(null, currentPage, pageSize, queryString, true);
    }

    @PostMapping("/add")
    public R addLendRecord(@RequestBody LendRecord lendRecord) {
        try {
            Book book = bookService.getById(lendRecord.getBookid());
            if (book.getNumber() <= 0) {
                return new R(false, "借阅记录添加失败: 图书数量不足");
            }
            book.setNumber(book.getNumber() - 1);
            boolean updateSuccess = bookService.updateById(book);
            if (updateSuccess) {
                lendRecord.setLendtime(LocalDateTime.now(ZoneId.of("UTC+8")));
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
            if (lendid == null) {
                return new R(false, "借阅记录ID不能为空");
            }

            Integer bookid = lendService.getBookIdByLendId(lendid);
            lendService.updateBackTime(lendid, LocalDateTime.now(ZoneId.of("UTC+8")));

            Book book = bookService.getById(bookid);
            if (book == null) {
                return new R(false, "未找到对应的图书");
            }

            book.setNumber(book.getNumber() + 1);
            bookService.updateById(book);

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
