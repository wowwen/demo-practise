//package com.demo.dynamicdb.way1.fengbao.config;
//
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//@MapperScan(basePackages = "com.demo.dynamicdb.way1.fengbao.mapper.ds2", sqlSessionTemplateRef = "ds2SqlSessionTemplate")
//public class Datasource2Configuration {
//    @Value("${spring.datasource.ds2.url}")
//    private String url;
//    @Value("${spring.datasource.ds2.username}")
//    private String username;
//    @Value("${spring.datasource.ds2.password}")
//    private String password;
//    @Value("${spring.datasource.ds2.driverClassName}")
//    private String driverClassName;
//
//    static final String MAPPER_LOCATION = "classpath*:/mapper/ds2/*.xml";
//
//    @Bean(name = "ds2DataSource")
//    public DataSource getCloudDataSource() {
//        DataSource build = DataSourceBuilder.create()
//                .driverClassName(driverClassName)
//                .url(url)
//                .username(username)
//                .password(password)
//                .build();
//        return build;
//    }
//
//    /**
//     * 创建SessionFactory
//     */
//    @Bean(name = "ds2SqlSessionFactory")
//    public SqlSessionFactory cloudSqlSessionFactory(@Qualifier("ds2DataSource") DataSource dataSource) throws Exception {
//        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
//        return bean.getObject();
//    }
//
//    @Bean("ds2TransactionManger")
//    public DataSourceTransactionManager cloudTransactionManger(@Qualifier("ds2DataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "ds2SqlSessionTemplate")
//    public SqlSessionTemplate cloudSqlSessionTemplate(@Qualifier("ds2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}
