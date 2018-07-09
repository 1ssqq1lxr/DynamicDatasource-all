package com.jswym.spring.boot.starter.multidatasource;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author lxr
 * @create 2018-06-05 14:14
 **/
public abstract class AbstractDynamicDataSource extends AbstractDataSource  {



    private Object defaultTargetDataSource;

    private boolean lenientFallback = true;

    private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();

    private Map<Object, DataSource> resolvedDataSources;

    private DataSource resolvedDefaultDataSource;

    public void setResolvedDefaultDataSource(DataSource resolvedDefaultDataSource) {
        this.resolvedDefaultDataSource = resolvedDefaultDataSource;
    }

    public void setResolvedDataSources(Map<Object, DataSource> resolvedDataSources) {
        this.resolvedDataSources = resolvedDataSources;
    }


    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    public void setLenientFallback(boolean lenientFallback) {
        this.lenientFallback = lenientFallback;
    }

    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
        this.dataSourceLookup = (dataSourceLookup != null ? dataSourceLookup : new JndiDataSourceLookup());
    }


    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        return lookupKey;
    }

    protected DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
        if (dataSource instanceof DataSource) {
            return (DataSource) dataSource;
        }
        else if (dataSource instanceof String) {
            return this.dataSourceLookup.getDataSource((String) dataSource);
        }
        else {
            throw new IllegalArgumentException(
                "Illegal data source value - only [javax.sql.DataSource] and String supported: " + dataSource);
        }
    }


    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return determineTargetDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || determineTargetDataSource().isWrapperFor(iface));
    }

    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = determineCurrentLookupKey();
        if(lookupKey == null){
             return this.resolvedDefaultDataSource;
        }
        DataSource dataSource = this.resolvedDataSources.get(lookupKey);
        return dataSource;
    }

    protected abstract Object determineCurrentLookupKey();
}
