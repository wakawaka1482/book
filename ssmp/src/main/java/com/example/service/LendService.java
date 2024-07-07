package com.example.service;

import com.example.entity.LendRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface LendService {

    List<LendRecord> findLendRecordsByUserId(int userid, int pageSize, int offset);

    List<LendRecord> findAllLendRecords(int pageSize, int offset);

    int countLendRecordsByUserId(int userid);

    int countAllLendRecords();

    boolean addLendRecord(LendRecord lendRecord);

    void updateBackTime(Integer lendid, LocalDateTime backtime);

    List<LendRecord> findLendRecordsByBookId(String bookId, int pageSize, int offset);

    int countLendRecordsByBookId(String bookId);

    boolean deleteLend(Integer lendid);

    Integer getBookIdByLendId(Integer lendid);
}
