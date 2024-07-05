package com.example.service.impl;

import com.example.entity.LendRecord;
import com.example.mapper.LendRecordMapper;
import com.example.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LendServiceImpl implements LendService {

    @Autowired
    private LendRecordMapper lendRecordMapper;

    @Override
    public List<LendRecord> findLendRecordsByUserId(int userid, int pageSize, int offset) {
        return lendRecordMapper.findLendRecordsByUserIdAndBacktimeIsNull(userid, pageSize, offset);
    }

    @Override
    public List<LendRecord> findAllLendRecords(int pageSize, int offset) {
        return lendRecordMapper.findAllLendRecords(pageSize, offset);
    }

    @Override
    public int countLendRecordsByUserId(int userid) {
        return lendRecordMapper.countLendRecordsByUserIdAndBacktimeIsNull(userid);
    }

    @Override
    public int countAllLendRecords() {
        return lendRecordMapper.countAllLendRecords();
    }

    @Override
    public boolean addLendRecord(LendRecord lendRecord) {
        return lendRecordMapper.insertLendRecord(lendRecord) > 0;
    }

    @Override
    public void updateBackTime(Integer lendid, LocalDateTime backtime) {
        lendRecordMapper.updateBackTime(lendid, backtime);
    }

    @Override
    public List<LendRecord> findLendRecordsByBookId(String bookId, int pageSize, int offset) {
        return lendRecordMapper.findLendRecordsByBookId(bookId, pageSize, offset);
    }

    @Override
    public int countLendRecordsByBookId(String bookId) {
        return lendRecordMapper.countLendRecordsByBookId(bookId);
    }

    @Override
    public boolean deleteLend(Integer lendid) {
        int rowsAffected = lendRecordMapper.deleteLendById(lendid);
        return rowsAffected > 0;
    }
}
