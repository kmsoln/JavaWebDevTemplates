package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Configuration class for setting up persistence-related beans and properties.
 *
 * <p>
 * This class configures the data source, entity manager factory, and transaction manager for JPA.
 * It reads database connection properties from an external properties file.
 * </p>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.repository")
@PropertySource({"classpath:application.properties"})
public class PersistenceConfig {

    @Value("${spring.datasource.driver}")
    private String dbDriver;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private boolean hibernateShowSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateHbm2ddlAuto;

    /**
     * Bean definition for the DataSource.
     *
     * <p>
     * This method sets up the data source using the properties defined in the application.properties file.
     * </p>
     *
     * @return DataSource bean
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    /**
     * Bean definition for the EntityManagerFactory.
     *
     * <p>
     * This method sets up the entity manager factory using the configured data source and Hibernate properties.
     * </p>
     *
     * @param dataSource the data source bean
     * @return EntityManagerFactory bean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(hibernateDialect);
        vendorAdapter.setShowSql(hibernateShowSql);
        vendorAdapter.setGenerateDdl("update".equalsIgnoreCase(hibernateHbm2ddlAuto));
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    /**
     * Bean definition for the Spring Transaction Manager.
     *
     * <p>
     * This method sets up the transaction manager for JPA using the configured entity manager factory.
     * </p>
     *
     * @param entityManagerFactory the entity manager factory bean
     * @return TransactionManager bean
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
