package com.example.demo.result;

import lombok.Data;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-25
 * @Description: todo
 */
@Data
public class JsonResult {
    private Integer code;

    private String message;

    private Object data;

    private JsonResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{ \"code\"=" + code +
                ", \"message\"=\"" + message +
                "\", \"data\"=\"" + data + "\"}";
    }
    public static JsonResult ok() {
        return JsonResult.ok("操作成功", "");
    }
    public static JsonResult ok(String message) {
        return ok(message,"");
    }

    public static JsonResult ok(Object data) {
        return ok("操作成功",data);
    }


    public static JsonResult error() {
        return JsonResult.error("操作失败", "");
    }


    public static JsonResult ok(String message, Object data) {
        return new JsonResult(0, message, data);
    }

    public static JsonResult error(String message, Object data) {
        return new JsonResult(-1, message, data);
    }
    private static JsonResult error(int code, String message, Object data) {
        return new JsonResult(code, message, data);
    }
}
