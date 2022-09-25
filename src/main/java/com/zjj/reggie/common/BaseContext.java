package com.zjj.reggie.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getId(){

        return threadLocal.get();
    }

    public static void setId(Long id){
        threadLocal.set(id);
    }
}
