package com.jswym.spring.boot.starter.multidatasource.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DataSourceProperties{

    /**
     * 机构编码
     */
    private String orgcode;
    /**
     * 是否生效
     */
    private boolean enabled;

    /**
     * 是否是默认数据库  配置多个 默认选择第一个
     */
    private boolean defaultDatasource;

    /**
     * 连接超时
     */
    private  long connectionTimeout;
    /**
     * 校验超时
     */
    private  long validationTimeout;
    /**
     * 断开连接等
     */
    private  long idleTimeout;
    /**
     * 连接被占用的超时时间
     */
    private  long leakDetectionThreshold;
    /**
     * 最大存活时间
     */
    private  long maxLifetime;
    /**
     * 连接池最大连接数
     */
    private  int maxPoolSize;

    /**
     * 最小存活时间
     */
    private  int minIdle;
    /**
     * 用户名
     */
    private  String username;
    /**
     * 密码
     */
    private  String password;
    /**
     * jdbc url
     */
    private  String jdbcUrl;

}
