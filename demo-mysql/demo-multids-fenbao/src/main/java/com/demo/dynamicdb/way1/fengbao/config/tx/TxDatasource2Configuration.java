package com.demo.dynamicdb.way1.fengbao.config.tx;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = TxDatasource2Configuration.PACKAGE, sqlSessionTemplateRef = "ds2SqlSessionTemplate")
public class TxDatasource2Configuration {
    static final String PACKAGE = "com.demo.dynamicdb.way1.fengbao.mapper.ds2";

    static final String MAPPER_LOCATION = "classpath*:/mapper/ds2/*.xml";

    @Bean(name = "ds2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ds2")
    public DataSource getEdgeDataSource() {
        //采用轻量级的分布式事务管理器Atomikos时，返回的是构建的Atomikos的数据库实例对象
        return new AtomikosDataSourceBean();
    }

    /**
     * 创建SessionFactory
     */
    @Bean(name = "ds2SqlSessionFactory")
    public SqlSessionFactory cloudSqlSessionFactory(@Qualifier("ds2DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return bean.getObject();
    }

//    @Bean("ds2TransactionManger")
//    public DataSourceTransactionManager cloudTransactionManger(@Qualifier("ds2DataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Bean(name = "ds2SqlSessionTemplate")
    public SqlSessionTemplate cloudSqlSessionTemplate(@Qualifier("ds2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
