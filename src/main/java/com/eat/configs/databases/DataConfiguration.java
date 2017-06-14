package com.eat.configs.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
@ComponentScan("com.eat")
@EnableJpaRepositories(basePackages = "com.eat.repositories.sql")
@EnableTransactionManagement
@EnableAsync
@EntityScan("com.eat")
public class DataConfiguration extends HikariConfig implements AsyncConfigurer {

    @Autowired
    private Environment environment;

    @Value(value = "${spring.datasource.hikari.jdbc-url}")
    private String url;

    @Value(value = "${spring.datasource.hikari.username}")
    private String username;

    @Value(value = "${spring.datasource.hikari.password}")
    private String password;

    @Value(value = "${spring.datasource.hikari.data-source-class-name}")
    private String dataSourceClassName;

    @Bean
    public DataSource dataSource() {
        
        Properties dataSourceProperties = new Properties();
        dataSourceProperties.put("url", url);
        dataSourceProperties.put("user", username);
        dataSourceProperties.put("password", password);

        Properties configProperties = new Properties();
        configProperties.put("dataSourceClassName", dataSourceClassName);
        configProperties.put("poolName", "MySqlConnectionPool");
        configProperties.put("maximumPoolSize", 100);
        /*configProperties.put("connectionTimeout", 10000);
        configProperties.put("minimumIdle", minimumIdle);
        configProperties.put("idleTimeout", idleTimeout);*/
        configProperties.put("dataSourceProperties", dataSourceProperties);

        HikariConfig hikariConfig = new HikariConfig(configProperties);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}