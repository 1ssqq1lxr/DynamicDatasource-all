package com.jswym.spring.boot.starter.multidatasource;

import com.jswym.spring.boot.starter.multidatasource.properties.MultiDatasourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * @author lxr
 * @create 2018-06-09 8:48
 **/
@Aspect
@Order(-1)// 保证该AOP在@Transactional之前执行
@Slf4j
public class DataSourceAspect {

    private final DynamicIdSelector dynamicIdSelector;

    private final MultiDatasourceProperties orgDsProperties;

    public DataSourceAspect(DynamicIdSelector dynamicIdSelector, MultiDatasourceProperties orgDsProperties) {
        this.dynamicIdSelector = dynamicIdSelector;
        this.orgDsProperties = orgDsProperties;
    }

    @Around("@annotation(com.jswym.microservice.uaa.datasource.annocation.AllDatasource)")
    public Object changeDataSource(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("使用多数据源操作 进入环绕通知目标方法名称:{}  参数:{}",pjp.getSignature().getName(),pjp.getArgs());
        orgDsProperties.getAllCode()
            .ifPresent(orgcodes -> {
                for (String code:orgcodes){
                    try {
                        dynamicIdSelector.setOrgCode(code);
                        pjp.proceed();
                    }
                    catch (Throwable e){

                    }
                    finally {
                        dynamicIdSelector.remove();
                    }
                }
            });
        return null;
    }
}
