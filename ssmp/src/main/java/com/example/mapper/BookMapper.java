package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Book;
import com.example.dto.BookTypeCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    @Select("SELECT name FROM sp_book WHERE id = #{id}")
    String findBookNameById(@Param("id") Integer id);

    @Select("SELECT image FROM sp_book WHERE id = #{id}")
    byte[] findBookImageById(@Param("id") Integer id);

    @Select("SELECT * FROM sp_book WHERE id = #{bookId}")
    Book selectById(Integer bookId);

    @Select("SELECT type AS type, COUNT(*) AS count FROM sp_book GROUP BY type")
    List<BookTypeCount> countBooksByType();
}
