package com.jswym.spring.boot.starter.multidatasource.annocation;

import java.lang.annotation.*;

/**
 * @author lxr
 * @create 2018-04-14 15:34
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AllDatasource {
}
