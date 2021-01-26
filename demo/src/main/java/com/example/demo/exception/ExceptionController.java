package com.example.demo.exception;

import com.example.demo.result.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-26
 * @Description: todo
 */
@RestControllerAdvice
public class ExceptionController {
    /**
     * 处理系统异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public JsonResult handlerException(Exception ex) {
        ex.printStackTrace();
        return JsonResult.error("系统异常", ex.getMessage());
    }

    /**
     * 处理自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MyException.class)
    public JsonResult handlerMyException(MyException ex) {
        ex.printStackTrace();
        return JsonResult.error("自定义异常", ex.getMessage());
    }
}
