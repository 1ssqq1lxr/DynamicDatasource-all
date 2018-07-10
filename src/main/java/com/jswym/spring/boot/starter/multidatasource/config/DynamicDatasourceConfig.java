package com.jswym.spring.boot.starter.multidatasource.config;

import com.jswym.spring.boot.starter.multidatasource.*;
import com.jswym.spring.boot.starter.multidatasource.filter.WymFilter;
import com.jswym.spring.boot.starter.multidatasource.properties.MultiDatasourceProperties;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.DataSource;

/**
 * 描述: TODO:
 *
 * 版本: 1.0 JDK: since 1.8
 */

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({MultiDatasourceProperties.class})
public class DynamicDatasourceConfig {

    /**
     * 配置事物管理器 默认jpa 如果是mybatis 请覆盖此配置
     *
     * @return
     */
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManagerPrimary(@Autowired LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean,@Autowired DataSource datasource) {
        localContainerEntityManagerFactoryBean.setDataSource(datasource);
        return new JpaTransactionManager(localContainerEntityManagerFactoryBean.getObject());
    }

    /**
     * 初始化数据源管理器
     */
    @Bean
    @ConditionalOnBean({MultiDatasourceProperties.class})
//    @ConditionalOnProperty(name = "enable",prefix ="com.wym",havingValue="true")
    public DataSourceManager initDataSourceManager(@Autowired MultiDatasourceProperties orgDsProperties){
        return new DataSourceManager(orgDsProperties);
    }

    /**
     * 初始化动态路由
     */
    @Bean("dynamicDataSource")
    @Primary
    @ConditionalOnBean({DynamicIdSelector.class,DataSourceManager.class,DataSource.class})
    public DataSource initAbstractRoutingDataSource(@Autowired DynamicIdSelector dynamicIdSelector,@Autowired DataSourceManager dataSourceManager){
        DynamicDataSource abstractRoutingDataSource = new DynamicDataSource(dynamicIdSelector,dataSourceManager);
        return  abstractRoutingDataSource;
    }

    /**
     * 初始化动态路由id选择
     */
    @Bean
    @ConditionalOnMissingBean(value = {DynamicIdSelector.class})
    public DynamicIdSelector initDynamicIdSelector(){
        return  new DafultDynamicIdSelector();
    }


    /**
     * 初始化全局拦截器
     */
    @Bean
    @ConditionalOnBean(value = {DynamicIdSelector.class})
    public OncePerRequestFilter initFilter(@Autowired DynamicIdSelector dynamicIdSelector){
        return  new WymFilter(dynamicIdSelector);
    }


//    /**
//     * 事务Aop拦截切面
//     */
//    @Bean
//    @ConditionalOnBean(value = {DynamicIdSelector.class,MultiDatasourceProperties.class})
//    public DataSourceAspect initAspect(@Autowired DynamicIdSelector dynamicIdSelector, @Autowired MultiDatasourceProperties multiDatasourceProperties){
//        return  new DataSourceAspect(dynamicIdSelector,multiDatasourceProperties);
//    }




}
