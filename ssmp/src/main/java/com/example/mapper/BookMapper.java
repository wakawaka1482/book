package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dto.BookLendCount;
import com.example.entity.Book;
import com.example.dto.BookTypeCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    @Select("SELECT name FROM sp_book WHERE id = #{id}")
    String findBookNameById(@Param("id") Integer id);

    @Select("SELECT * FROM sp_book WHERE id = #{bookId}")
    Book selectById(Integer bookId);

    @Select("SELECT type AS type, COUNT(*) AS count FROM sp_book GROUP BY type")
    List<BookTypeCount> countBooksByType();

    @Select("SELECT b.name AS bookname, COUNT(*) AS count FROM sp_lend_record lr JOIN sp_book b ON lr.bookid = b.id GROUP BY lr.bookid")
    List<BookLendCount> countBookLendsWithNames();


}
