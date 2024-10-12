package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义切面类,实现公共字段自动填充处理
 */
@Component
@Aspect//定义为切面类
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")//定义切点表达式
    public void autoFillPointCut(){}


    /**
     *前置通知
     */
    @Before("autoFillPointCut()")//指定切入点
    public void autFill(JoinPoint joinPoint)  {
        log.info("开始进行公共字段的自动填充...");

        //获取当前被拦截的方法上的数据库操作类型(UPDATE 还是 INSERT)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取当前被拦截的方法参数(实体对象)
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object arg = args[0];

        //准备赋值的数据(当前操作人id 和 当前时间)
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据不同的操作类型,为对应的属性来赋值(通过反射)
        if (operationType == OperationType.INSERT){
            //为4个公共字段赋值

        }
        else if (operationType == OperationType.UPDATE){
            //为两个公共字段赋值

        }


    }

}
