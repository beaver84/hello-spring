package hello.hellospring.config;

import hello.hellospring.config.typeHandler.LocalDateTimeTypeHandler;
import hello.hellospring.config.typeHandler.LocalDateTypeHandler;
import hello.hellospring.config.typeHandler.LocalTimeTypeHandler;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Lazy
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {MybatisAutoConfiguration.class})
public class AppMybatisConfiguration {

    private Logger log = LogManager.getLogger(this.getClass());

    public static final Integer QUERY_TIMEOUT = 10;

    @ConfigurationProperties(prefix = "moonapp.datasource")
    @Bean(name = "helloAppDatasourceProperties")
    public Properties moonAppDatasourceProperties() {
        return new Properties();
    }

    @Bean(name = "moonAppDataSource")
    public DataSource moonAppDataSource() throws Exception {
        Properties properties = moonAppDatasourceProperties();
        log.info("moonAppDataSource Properties {}", properties);
        BasicDataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
        dataSource.setDefaultQueryTimeout(QUERY_TIMEOUT);
        return dataSource;
    }

    @Bean(name = "moonAppTransactionManager")
    public PlatformTransactionManager moonAppTransactionManager() throws Exception {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(moonAppDataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

    @Bean(name = "moonAppSqlSessionFactory")
    public SqlSessionFactory moonAppSqlSessionFactory() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(moonAppDataSource());
        sessionFactoryBean.setTypeHandlers(
                new LocalDateTypeHandler(),
                new LocalDateTimeTypeHandler(),
                new LocalTimeTypeHandler());

        //model entity alias 감지를 위한 패키지 지정
        sessionFactoryBean.setTypeAliasesPackage("hello.hellospring.domain");

        Resource[] authMappers = resolver.getResources("sqlmapper/app/**.xml");
        Resource[] defaultMappers = resolver.getResources("sqlmapper/**.xml");

        Resource[] mappers = ArrayUtils.addAll(authMappers, defaultMappers);
        sessionFactoryBean.setMapperLocations(mappers);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        configuration.setDefaultStatementTimeout(QUERY_TIMEOUT);

        //entity 카멜명 필드와 mybatis 쿼리문의 snake명 필드명을 맵핑
        configuration.setMapUnderscoreToCamelCase(true);

        //쿼리의 결과 필드가 null 인경우, null 누락되어서 response 에 나갓는것을 방지
        configuration.setCallSettersOnNulls(true);

        //쿼리에 보내는 파라메터가 null인 경우, 오류 발생하는 것 방지(설정 필요한지 고려 필요)
        configuration.setJdbcTypeForNull(null);

        sessionFactoryBean.setConfiguration(configuration);
        return sessionFactoryBean.getObject();
    }
}