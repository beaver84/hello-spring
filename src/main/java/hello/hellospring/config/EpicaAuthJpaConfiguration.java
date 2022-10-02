package hello.hellospring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Lazy
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "hello.hellospring.repository", entityManagerFactoryRef = "epicaAuthEntityManagerFactory")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class EpicaAuthJpaConfiguration {
    private final Properties properties;

    @Autowired
    public EpicaAuthJpaConfiguration(@Qualifier("epicaAuthDatasourceProperties") Properties properties) {
        this.properties = properties;
    }

    @Bean(name = "epicaAuthJpaDatasource")
    public DataSource epicaauthJpaDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getProperty("driver-class-name"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));

        return dataSource;
    }

    @Bean(name = "epicaAuthEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean epicaAuthEntityManagerFactory() throws Exception {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(epicaauthJpaDatasource());
        em.setPackagesToScan("hello.hellospring.domain");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(epicaauthJpaDataSourceProperties());

        return em;
    }

    @ConfigurationProperties(prefix = "epicaauth.jpa")
    @Bean(name = "epicaauthJpaDataSourceProperties")
    public Properties epicaauthJpaDataSourceProperties() {
        return new Properties();
    }

    @Bean(name = "epicaauthJpaTransactionManager")
    public PlatformTransactionManager epicaauthJpaTransactionManager() throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(epicaAuthEntityManagerFactory().getObject());
        return transactionManager;
    }
}
