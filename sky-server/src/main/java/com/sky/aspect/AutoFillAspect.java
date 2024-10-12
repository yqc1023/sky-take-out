package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    public void autFill(JoinPoint joinPoint) throws NoSuchMethodException {
        log.info("开始进行公共字段的自动填充...");

        //获取当前被拦截的方法上的数据库操作类型(UPDATE 还是 INSERT)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象(加上要获取的注解的.class)
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取当前被拦截的方法参数(实体对象)
        //默认将实体对象作为方法的第一个参数
        Object[] args = joinPoint.getArgs();//获得一个装着所有参数的数组
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];//(entity是Object类型的实体类对象,没有getset方法,想给里面的东西赋值得用反射)

        //准备赋值的数据(当前操作人id 和 当前时间)
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据不同的操作类型,为对应的属性来赋值(通过反射)
        if (operationType == OperationType.INSERT){//反射三要素 : 调用什么对象 形参是什么  返回什么

            //为4个公共字段赋值 (实体类对象.getClass()获取该实体对象的class文件)
            //                (.getDeclaredMethod()从此clss文件中调用一个(Declared无论是公共还是私有都可获取)方法的对象        ---调用什么对象
            //                "setCreateTime"获取的方法对象的名字    LocalDateTime.class此方法的形参类型的.class形式    ---形参是什么
            //                 这几个set方法都没有返回值,所以不用写     ---调用什么对象

            try {//"setCreateTime"已经被封装为常量类AutoFillConstant中
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //通过反射给对象属性赋值
                //将异常以try/catch形式抛出
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (operationType == OperationType.UPDATE){
            //为两个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, LocalDateTime.class);

                //通过反射给对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
