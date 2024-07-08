package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.User;
import com.example.dto.AuthRequest;
import com.example.dto.PaginationDTO;
import com.example.handler.AuthHandler;
import com.example.service.AuthService;
import com.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthHandler authChain;

    //登录
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        AuthRequest request = new AuthRequest(username, password);
        authChain.handle(request);

        Map<String, Object> response = new HashMap<>();
        if (request.isAuthenticated()) {
            response.put("flag", true);
            response.put("msg", "登录成功");
            if ("admin".equals(request.getRole())) {
                response.put("redirectUrl", "admin_main.html?username=" + username);
            } else {
                response.put("redirectUrl", "user_main.html?username=" + username);
            }
            return ResponseEntity.ok(response);
        } else {
            response.put("flag", false);
            response.put("msg", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    //注册
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> registerData) {
        String username = (String) registerData.get("username");
        String password = (String) registerData.get("password");
        String phone = (String) registerData.get("phone");
        String sex = (String) registerData.get("sex");

        boolean isRegistered = authService.register(username, password, phone, sex);

        Map<String, Object> response = new HashMap<>();
        if (isRegistered) {
            response.put("flag", true);
            response.put("msg", "注册成功");
            response.put("redirectUrl", "user_main.html?username=" + username);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("flag", false);
            response.put("msg", "注册失败，用户名可能已存在");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //获取用户id
    @GetMapping("/getUserId")
    public ResponseEntity<Map<String, Object>> getUserIdByUsername(@RequestParam String username) {
        Integer userId = authService.findUserIdByUsername(username);
        Map<String, Object> response = new HashMap<>();
        if (userId != null) {
            response.put("flag", true);
            response.put("data", userId);
            return ResponseEntity.ok(response);
        } else {
            response.put("flag", false);
            response.put("msg", "用户不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //通过userid获取用户信息
    @GetMapping("/getUserInfoByUsername")
    public R getUserInfoByUsername(@RequestParam String username) {
        Integer userId = authService.findUserIdByUsername(username);
        if (userId != null) {
            return new R(true, userId, "获取用户信息成功");
        } else {
            return new R(false, "用户不存在");
        }
    }

    //修改密码
    @PostMapping("/updatepwd")
    public R updatePassword(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String oldPassword = params.get("oldpwd");
            String newPassword = params.get("newpwd");

            boolean result = authService.updatePassword(username, oldPassword, newPassword);
            if (result) {
                return new R(true, "密码更新成功");
            } else {
                return new R(false, "密码更新失败，旧密码错误");
            }
        } catch (Exception e) {
            return new R(false, "密码更新失败: " + e.getMessage());
        }
    }
    //获取全部用户
    @PostMapping("/users")
    public R getAllUsers(@RequestBody PaginationDTO paginationDTO) {
        IPage<User> userPage = authService.getAllUsers(paginationDTO.getCurrentPage(), paginationDTO.getPageSize());
        return new R(true, userPage);
    }

    //删除用户
    @DeleteMapping("/delete/{id}")
    public R deleteUser(@PathVariable int id) {
        boolean removed = authService.removeById(id);
        if (removed) {
            return new R(true, "删除成功");
        } else {
            return new R(false, "删除失败");
        }
    }

    //更改用户信息
    @GetMapping("/update/{id}")
    public R getUserById(@PathVariable int id) {
        User user = authService.getUserById(id);
        if (user != null) {
            return new R(true, user);
        } else {
            return new R(false, "用户数据获取失败，用户不存在");
        }
    }
    //更改用户信息
    @PostMapping("/update")
    public R updateUser(@RequestBody User user) {
        boolean result = authService.updateUser(user);
        if (result) {
            return new R(true, user);
        } else {
            return new R(false, "用户更新失败，用户不存在");
        }
    }

}
