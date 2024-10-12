package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义注解,用于标识某个方法需要进行功能字段填充处理
 */
@Retention(RetentionPolicy.RUNTIME)//表示此注解在运行时生效
@Target(ElementType.METHOD)//表示此注解在方法上生效(加在什么位置)
public @interface AutoFill {
    //数据库操作类型: UPDATE INSERT
    OperationType value();
}
