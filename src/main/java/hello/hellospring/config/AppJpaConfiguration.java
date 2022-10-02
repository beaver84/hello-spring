package hello.hellospring.config;

import java.util.Properties;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
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

@Configuration
@Lazy
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "hello.hellospring.repository", entityManagerFactoryRef = "appEntityManagerFactory")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AppJpaConfiguration {

    private Properties properties;

    @Autowired
    public AppJpaConfiguration(@Qualifier("moonAppDatasourceProperties") Properties properties) {
        this.properties = properties;
    }

    @Bean(name = "moonAppJpaDatasource")
    public DataSource moonAppJpaDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getProperty("driver-class-name"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));

        return dataSource;
    }

    @Bean(name = "appEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean appEntityManager() throws Exception {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(moonAppJpaDatasource());
        em.setPackagesToScan("hello.hellospring.domain");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(moonAppJpaDataSourceProperties());

        return em;
    }

    @ConfigurationProperties(prefix = "moonapp.jpa")
    @Bean(name = "moonAppJpaDataSourceProperties")
    public Properties moonAppJpaDataSourceProperties() {
        return new Properties();
    }

    @Bean(name = "moonAppJpaTransactionManager")
    public PlatformTransactionManager moonAppJpaTransactionManager() throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(appEntityManager().getObject());
        return transactionManager;
    }
}

