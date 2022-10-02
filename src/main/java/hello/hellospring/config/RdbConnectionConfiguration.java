package hello.hellospring.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Lazy
@EnableTransactionManagement
public class RdbConnectionConfiguration {

    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(
            @Qualifier("moonAppTransactionManager") PlatformTransactionManager appTestSqlSessionFactory,
            @Qualifier("moonAppJpaTransactionManager") PlatformTransactionManager appJpaTestTransactionManager,
            @Qualifier("epicaauthJpaTransactionManager") PlatformTransactionManager epicaauthJpaTransactionManager,
            @Qualifier("epicaAuthTransactionManager") PlatformTransactionManager epicaAuthTransactionManager) {
        return new ChainedTransactionManager(
                appTestSqlSessionFactory, appJpaTestTransactionManager, epicaauthJpaTransactionManager,
                epicaAuthTransactionManager);
    }
}