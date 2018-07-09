package com.jswym.spring.boot.starter.multidatasource;

/**
 * @author lxr
 * @create 2018-06-05 10:36
 **/
public class DatasourceNotFindException extends RuntimeException {
    public DatasourceNotFindException(String message) {
        super(message);
    }
}
