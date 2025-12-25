package com.example.help_me_chat.common;

import lombok.Data;

/**
 * 通用返回结果（对齐前端BaseResponse）
 */
@Data
public class BaseResponse<T> {
    /**
     * 状态码（200=成功，500=失败，401=未登录）
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据体
     */
    private T data;

    // 成功返回（带数据）
    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(200);
        response.setMsg("操作成功");
        response.setData(data);
        return response;
    }

    // 成功返回（无数据）
    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    // 失败返回
    public static <T> BaseResponse<T> error(String msg) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(500);
        response.setMsg(msg);
        response.setData(null);
        return response;
    }

    // 未登录返回
    public static <T> BaseResponse<T> unAuth() {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(401);
        response.setMsg("未登录或Token过期");
        response.setData(null);
        return response;
    }
}