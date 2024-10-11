package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final HttpMessageConverters messageConverters;

    public GlobalExceptionHandler(HttpMessageConverters messageConverters) {
        this.messageConverters = messageConverters;
    }

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */

    //定义一个全局异常处理器
    //形参写异常的类型
    //根据控制台返回的异常信息 Duplicate entry 'zhangsan' for key 'employee.idx_username'
    //处理捕获的异常,上述异常信息是指 'zhangsan'名字重复(Duplicate entry)
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message= ex.getMessage();
       if ("Duplicate entry".contains(message)){
           String[] split = message.split(" ");
           String username = split[2];
           String msg = username + MessageConstant.ALREADY_EXISTS;
           return Result.error(msg);
       }

        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }


}
