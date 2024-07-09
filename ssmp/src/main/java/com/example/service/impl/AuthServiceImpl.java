package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean authenticate(String username, String password) {
        User user = userMapper.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public boolean register(String username, String password, String phone, String sex) {
        if (userMapper.findByUsername(username) != null) {
            return false; // 用户名已存在
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setPhone(phone);
        newUser.setSex(sex);
        newUser.setRole("user"); // 默认角色为用户
        userMapper.insertUser(newUser);
        return true;
    }

    @Override
    public String getRole(String username) {
        User user = userMapper.findByUsername(username);
        return user != null ? user.getRole() : null;
    }

    @Override
    public User getUserById(int userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Integer findUserIdByUsername(String username) {

        return userMapper.findUserIdByUsername(username);
    }

    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        // 获取用户信息
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!user.getPassword().equals(oldPassword)) {
            return false; // 旧密码错误
        }

        // 更新密码
        userMapper.updatePassword(user.getId(), newPassword);
        return true;
    }

    public IPage<User> getAllUsers(int currentPage, int pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "user");
        return userMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean removeById(int id) {

        return userMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateById(user) > 0;
    }
}
