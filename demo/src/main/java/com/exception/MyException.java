package com.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-25
 * @Description: todo
 */
@Data
@ToString
@NoArgsConstructor
public class MyException extends Exception {

    private Integer code;

    private String message;

    public MyException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
