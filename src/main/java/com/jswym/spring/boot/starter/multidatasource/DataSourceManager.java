package com.jswym.spring.boot.starter.multidatasource;

import com.jswym.spring.boot.starter.multidatasource.properties.DataSourceProperties;
import com.jswym.spring.boot.starter.multidatasource.properties.MultiDatasourceProperties;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceManager {

    private String DRIVER_NAME  = "com.mysql.jdbc.Driver" ;

    private Map<Object,DataSource> dataSourceMap = new ConcurrentHashMap<>();

    public DataSourceManager(MultiDatasourceProperties orgDsProperties) {
        this.orgDsProperties = orgDsProperties;
    }


    public Map<Object, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    private final MultiDatasourceProperties orgDsProperties;

    public String getDaefaultOrgcode(){
        return Optional.ofNullable(orgDsProperties.getMultidatasource())
                .map(configs ->
                        configs.stream()
                                .filter(config -> config.isEnabled()&&config.isDefaultDatasource())
                                .findFirst()
                                .map(dataSourceProperties -> dataSourceProperties.getOrgcode())
                                .orElse(null)
                ).orElse(null);
    }


    public DataSource getDefault(){
        return Optional.ofNullable(orgDsProperties.getMultidatasource())
                .map(configs ->
                        configs.stream()
                                .filter(config -> config.isEnabled()&&config.isDefaultDatasource())
                                .findFirst()
                                .map(this::createDefaultDatasource)
                                .orElseThrow(() -> new DatasourceNotFindException("未发现默认数据源"))
                ).orElseThrow(() -> new DatasourceNotFindException("未发现默认数据源")) ;
    }

    private DataSource createDefaultDatasource(DataSourceProperties dataSourceProperties) {
        DataSource datasource = createDatasource(dataSourceProperties);
        dataSourceMap.putIfAbsent(dataSourceProperties.getOrgcode(),datasource);
        return datasource;
    }

    public DataSource getDatasoourceByOrgCode(String orgcode) {
        return dataSourceMap.computeIfAbsent(orgcode, code -> findDatasource(code.toString()));
    }

    private DataSource findDatasource(String orgcode) {
        return orgDsProperties.getMultidatasource()
                .stream()
                .filter(config -> config.getOrgcode().equalsIgnoreCase(orgcode))
                .findFirst().map(this::createDatasource).orElseThrow(() ->new DatasourceNotFindException("未找到匹配数据源【"+orgcode+"】"));
    }

    private DataSource createDatasource(DataSourceProperties config) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(DRIVER_NAME);
        hikariDataSource.setJdbcUrl(config.getJdbcUrl());
        hikariDataSource.setUsername(config.getUsername());
        hikariDataSource.setPassword(config.getPassword());
        if(config.getConnectionTimeout()>0)
            hikariDataSource.setConnectionTimeout(config.getConnectionTimeout());
        if(config.getValidationTimeout()>0)
            hikariDataSource.setValidationTimeout(config.getValidationTimeout());
        if(config.getIdleTimeout()>0)
            hikariDataSource.setIdleTimeout(config.getIdleTimeout());
        if(config.getLeakDetectionThreshold()>0)
            hikariDataSource.setLeakDetectionThreshold(config.getLeakDetectionThreshold());
        if(config.getMaxLifetime()>0)
            hikariDataSource.setMaxLifetime(config.getMaxLifetime());
        if(config.getMaxPoolSize()>0)
            hikariDataSource.setMaximumPoolSize(config.getMaxPoolSize());
        if(config.getMinIdle()>0)
            hikariDataSource.setMinimumIdle(config.getMinIdle());
        return  hikariDataSource;
    }
}
