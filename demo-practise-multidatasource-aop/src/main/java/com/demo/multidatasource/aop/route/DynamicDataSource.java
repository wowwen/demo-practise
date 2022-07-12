package com.demo.multidatasource.aop.route;


import com.demo.multidatasource.aop.context.DataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        //获取CONTEXT_HOLDER中存的dataSource
        return DataSourceContextHolder.getDataSource();
    }
}