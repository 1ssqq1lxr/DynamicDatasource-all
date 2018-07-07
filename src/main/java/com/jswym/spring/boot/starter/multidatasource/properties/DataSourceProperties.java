package com.jswym.spring.boot.starter.multidatasource.properties;


public class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

    private long tenantId;

    private String orgcode;

}
