package com.jswym.spring.boot.starter.multidatasource;

/**
 * 线程上下文datasource选择器材
 *
 * @author lxr
 * @create 2018-04-27 17:42
 **/
public interface DynamicIdSelector {


    void setOrgCode(String orgCode);

    String getOrgCode();

    void remove();


}
