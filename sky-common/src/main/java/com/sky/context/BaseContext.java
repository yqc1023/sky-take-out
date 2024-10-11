package com.sky.context;

public class BaseContext {
    //ThreadLocal是一个线程的存储空间,是独立于其他线程的,只能在此线程内访问此存储空间,不能在线程外访问
    //客户端的每一次请求都会开启一个单独的线程,每一个线程都会有一个独立的存储空间ThreadLocal
    //将ThreadLocal封装后,调用方法时会自动创建其对象
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
