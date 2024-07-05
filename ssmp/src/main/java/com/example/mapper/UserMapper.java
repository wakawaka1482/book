package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM sp_user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO sp_user (username, password, phone, sex, role) VALUES (#{username}, #{password}, #{phone}, #{sex}, #{role})")
    void insertUser(User user);

    @Select("SELECT id FROM sp_user WHERE username = #{username}")
    Integer findUserIdByUsername(@Param("username") String username);

    @Select("SELECT username FROM sp_user WHERE id = #{id}")
    String findUsernameById(@Param("id") Integer id);

    @Select("SELECT username FROM sp_user WHERE id = #{userId}")
    String findUsernameByUserId(Integer userId);

    @Update("UPDATE sp_user SET password = #{newPassword} WHERE id = #{id}")
    void updatePassword(@Param("id") int id, @Param("newPassword") String newPassword);

    @Update("UPDATE sp_user SET username=#{username}, phone=#{phone}, sex=#{sex}, password=#{password}WHERE id=#{id}")
    int updateById(User user);

}
