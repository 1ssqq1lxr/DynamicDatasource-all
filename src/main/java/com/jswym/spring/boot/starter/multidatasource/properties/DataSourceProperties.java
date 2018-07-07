package com.jswym.spring.boot.starter.multidatasource.properties;


public class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

    /**
     * 租户id
     */
    private long tenantId;

    /**
     * 机构编码
     */
    private String orgcode;

}
