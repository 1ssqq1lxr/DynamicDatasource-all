package com.jswym.spring.boot.starter.multidatasource;


import javax.sql.DataSource;
import java.util.Optional;


/**
 * 动态数据源
 *
 * @author lxr
 * @create 2018-04-25 14:37
 **/
public class DynamicDataSource extends AbstractDynamicDataSource {

    private final DynamicIdSelector dynamicIdSelector;

    private final  DataSourceManager dataSourceManager;

    public DynamicDataSource(DynamicIdSelector dynamicIdSelector, DataSourceManager dataSourceManager) {
        this.dynamicIdSelector = dynamicIdSelector;
        this.dataSourceManager = dataSourceManager;
        super.setResolvedDefaultDataSource(dataSourceManager.getDefault());
        super.setResolvedDataSources(dataSourceManager.getDataSourceMap());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return  dynamicIdSelector.getOrgCode();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        return Optional.ofNullable(super.determineTargetDataSource())
            .orElseGet(() -> dataSourceManager.getDatasoourceByOrgCode((String) this.determineCurrentLookupKey()));
    }
}

