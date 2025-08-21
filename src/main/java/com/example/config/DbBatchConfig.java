package com.example.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DbBatchConfig extends DefaultBatchConfiguration {

    @Bean(name = "dataSourceBatch")
    public DataSource dataSourceBatch(
            @Value("${spring.datasource.batch.url}") String url,
            @Value("${spring.datasource.batch.username}") String userName,
            @Value("${spring.datasource.batch.password}") String password,
            @Value("${spring.datasource.batch.driver-class-name}") String driverClassName
    ) {
        return DataSourceBuilder.create().url(url).username(userName).password(password).driverClassName(driverClassName).type(HikariDataSource.class).build();
    }

    // Automatically manages transactions for the provided DataSource
    @Bean(name = "batchTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSourceBatch") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        transactionManager.setRollbackOnCommitFailure(true);
        return transactionManager;
    }

    // Store job information
    @Bean
    public JobRepository jobRepository(@Qualifier("dataSourceBatch") DataSource dataSource,@Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSourceBatch") DataSource dataSource,
                                                       @Value("${spring.batch.jdbc.schema}") String classPathResource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource(classPathResource));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }


    @Override
    protected @NonNull DataSource getDataSource() {
        String errorMessage = "";
        if (this.applicationContext.getBeansOfType(DataSource.class).isEmpty()) {
            throw new BatchConfigurationException("Unable to find a DataSource bean in the application context." + errorMessage);
        } else if (!this.applicationContext.containsBean("dataSourceBatch")) {
            throw new BatchConfigurationException(errorMessage);
        } else {
            return this.applicationContext.getBean("dataSourceBatch", DataSource.class);
        }
    }

    @Override
    protected @NonNull PlatformTransactionManager getTransactionManager() {
        return this.applicationContext.getBean("batchTransactionManager", PlatformTransactionManager.class);
    }
}
