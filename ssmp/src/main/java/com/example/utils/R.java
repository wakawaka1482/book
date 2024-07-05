package com.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    // 标志接口是否成功执行
    private Boolean flag;
    // 调用接口返回数据格式
    private Object data;
    // 返回状态信息
    private String msg;
    private boolean success;
    private String redirectUrl;
    private String message;

    public R(boolean success, String redirectUrl, String message) {
        this.success = success;
        this.redirectUrl = redirectUrl;
        this.message = message;
    }

    public R(Boolean flag) {
        this.flag = flag;
    }

    public R(Boolean flag, Object data) {
        this.flag = flag;
        this.data = data;
    }

    public R(Boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public R(String msg) {
        this.flag = false;
        this.msg = msg;
    }

    public R(boolean flag, Integer data, String msg) {
        this.flag = flag;
        this.data = data;
        this.msg = msg;
    }
}
