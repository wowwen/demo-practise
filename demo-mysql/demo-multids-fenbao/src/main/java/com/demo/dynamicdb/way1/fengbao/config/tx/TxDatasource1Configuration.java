/**
 * 添加分布式事务管理atomikos后的配置类
 */
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
@MapperScan(basePackages = TxDatasource1Configuration.PACKAGE, sqlSessionTemplateRef = "ds1SqlSessionTemplate")
public class TxDatasource1Configuration {
    static final String PACKAGE = "com.demo.dynamicdb.way1.fengbao.mapper.ds1";

    static final String MAPPER_LOCATION = "classpath*:/mapper/ds1/*.xml";

    /**
     * 除了上面的第一种方式构建，还可以像下面这样通过@ConfigurationProperties注解来读取属性
     * @return
     */
    @Bean(name = "ds1DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.ds1")
    public DataSource getEdgeDataSource() {
        //采用轻量级的分布式事务管理器Atomikos时，返回的是构建的Atomikos的数据库实例对象
       return new AtomikosDataSourceBean();
    }

    /**
     * 创建SessionFactory
     */
    @Bean(name = "ds1SqlSessionFactory")
    @Primary
    public SqlSessionFactory edgeSqlSessionFactory(@Qualifier("ds1DataSource") DataSource dataSource) throws Exception { //@Qualifier("ds1DataSource")要求哪个数据源
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return bean.getObject();
    }

//    @Bean("ds1TransactionManger")
//    @Primary
//    public DataSourceTransactionManager edgeTransactionManger(@Qualifier("ds1DataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Bean(name = "ds1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate edgeSqlSessionTemplate(@Qualifier("ds1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
