package com.jswym.spring.boot.starter.multidatasource.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 配置
 */
@ConfigurationProperties(prefix = MultiDatasourceProperties.MULTI_DATASOURCE_PREFIX)
public class MultiDatasourceProperties {

    public static final String  MULTI_DATASOURCE_PREFIX= "wym.multidatasource";

    /**
     * 是否生效
     */
    private boolean enabled;

    /**
     * 规则来源
     */
    private String gitSource;

    /**
     * 规则来源认证用户名
     */
    private String username;

    /**
     * 规则来源认证密码
     */
    private String password;

    /**
     * 多数据源
     */
    private Map<String, DataSourceProperties> datasourceMap;



}
