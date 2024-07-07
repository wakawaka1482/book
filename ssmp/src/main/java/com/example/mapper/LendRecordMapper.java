package com.example.mapper;

import com.example.entity.LendRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LendRecordMapper {

    @Select("SELECT * FROM sp_lend_record WHERE userid = #{userid} AND backtime IS NULL LIMIT #{pageSize} OFFSET #{offset}")
    List<LendRecord> findLendRecordsByUserIdAndBacktimeIsNull(@Param("userid") int userId, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM sp_lend_record WHERE userid = #{userid} AND backtime IS NULL")
    int countLendRecordsByUserIdAndBacktimeIsNull(@Param("userid") int userId);

    @Insert("INSERT INTO sp_lend_record (bookid, userid, lendtime) VALUES (#{bookid}, #{userid}, #{lendtime})")
    int insertLendRecord(LendRecord lendRecord);

    @Update("UPDATE sp_lend_record SET backtime = #{backtime} WHERE lendid = #{lendid}")
    void updateBackTime(@Param("lendid") Integer lendid, @Param("backtime") LocalDateTime backtime);

    @Select("SELECT * FROM SP_lend_record LIMIT #{pageSize} OFFSET #{offset}")
    List<LendRecord> findAllLendRecords(int pageSize, int offset);

    @Select("SELECT COUNT(*) FROM sp_lend_record")
    int countAllLendRecords();

    @Select("SELECT * FROM sp_lend_record WHERE bookid = #{bookId} LIMIT #{pageSize} OFFSET #{offset}")
    List<LendRecord> findLendRecordsByBookId(@Param("bookId") String bookId, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM SP_lend_record WHERE bookid = #{bookId}")
    int countLendRecordsByBookId(@Param("bookId") String bookId);

    @Delete("DELETE FROM sp_lend_record WHERE lendid = #{lendid}")
    int deleteLendById(int lendid);

    @Select("SELECT bookid FROM sp_lend_record WHERE lendid = #{lendid}")
    Integer getBookIdByLendId(Integer lendid);
}
