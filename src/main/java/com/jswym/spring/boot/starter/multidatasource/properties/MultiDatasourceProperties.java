package com.jswym.spring.boot.starter.multidatasource.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * 配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = MultiDatasourceProperties.MULTI_DATASOURCE_PREFIX)
public class MultiDatasourceProperties {

    public static final String  MULTI_DATASOURCE_PREFIX= "application.wym";


    private List<DataSourceProperties> multidatasource;


}
