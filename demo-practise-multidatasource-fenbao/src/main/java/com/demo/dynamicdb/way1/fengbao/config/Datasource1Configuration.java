package com.demo.dynamicdb.way1.fengbao.config;


import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.demo.dynamicdb.way1.fengbao.mapper.ds1", sqlSessionTemplateRef = "ds1SqlSessionTemplate")
public class Datasource1Configuration {
    @Value("${spring.datasource.ds1.url}")
    private String url;
    @Value("${spring.datasource.ds1.username}")
    private String username;
    @Value("${spring.datasource.ds1.password}")
    private String password;
    @Value("${spring.datasource.ds1.driverClassName}")
    private String driverClassName;

    static final String MAPPER_LOCATION = "classpath*:/mapper/ds1/*.xml";
    //static final String MAPPER_LOCATION ="classpath*:/edge/*.xml";

    @Bean(name = "ds1DataSource")
    @Primary
    public DataSource getEdgeDataSource() {
        DataSource build = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
        return build;
    }

    /**
     * 创建SessionFactory
     */
    @Bean(name = "ds1SqlSessionFactory")
    @Primary
    public SqlSessionFactory edgeSqlSessionFactory(@Qualifier("ds1DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return bean.getObject();
    }

    @Bean("ds1TransactionManger")
    @Primary
    public DataSourceTransactionManager edgeTransactionManger(@Qualifier("ds1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "ds1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate edgeSqlSessionTemplate(@Qualifier("ds1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
