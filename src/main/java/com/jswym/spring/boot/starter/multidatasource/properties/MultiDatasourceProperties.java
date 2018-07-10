package com.jswym.spring.boot.starter.multidatasource.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 配置
 */
@RefreshScope
@ConfigurationProperties(prefix = MultiDatasourceProperties.MULTI_DATASOURCE_PREFIX)
public class MultiDatasourceProperties {

    public static final String  MULTI_DATASOURCE_PREFIX= "com.wym";


    private List<DataSourceProperties> multidatasource;

    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @RefreshScope
    public List<DataSourceProperties> getMultidatasource() {
        return multidatasource;
    }

    public void setMultidatasource(List<DataSourceProperties> multidatasource) {
        this.multidatasource = multidatasource;
    }



    public Optional<Set<String>> getAllCode() {
        return Optional.ofNullable(this.multidatasource)
                .map(dataSourceProperties -> dataSourceProperties
                        .stream()
                        .filter(dataSource -> dataSource.isEnabled()&&!dataSource.isDefaultDatasource())
                        .map(dataSource -> dataSource.getOrgcode())
                        .collect(Collectors.toSet()));
    }
}
