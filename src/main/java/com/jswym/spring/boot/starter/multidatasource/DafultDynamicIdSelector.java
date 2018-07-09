package com.jswym.spring.boot.starter.multidatasource;

import org.springframework.stereotype.Service;

/**
 * @author lxr
 * @create 2018-04-27 17:44
 **/
@Service
public class DafultDynamicIdSelector implements DynamicIdSelector {

    protected static ThreadLocal<String> instCodeThreadLocal = new ThreadLocal<>();

    @Override
    public void setOrgCode(String orgCode) {
         instCodeThreadLocal.set(orgCode);
    }

    @Override
    public String getOrgCode() {
        return instCodeThreadLocal.get();
    }

    @Override
    public void remove() {
        instCodeThreadLocal.remove();
    }


}
