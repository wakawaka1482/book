package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.PaginationDTO;
import com.example.entity.User;

public interface AuthService extends IService<User> {
    boolean authenticate(String username, String password);
    boolean register(String username, String password, String phone, String sex);
    String getRole(String username);
    User getUserById(int userId);
    Integer findUserIdByUsername(String username);
    boolean updatePassword(String username, String oldPassword, String newPassword);
    IPage<User> getAllUsers(int currentPage, int pageSize);
    boolean removeById(int id);
    boolean updateUser(User user);

    IPage<User> queryUsers(PaginationDTO paginationDTO);
}
