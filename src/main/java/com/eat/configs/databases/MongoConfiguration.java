package com.eat.configs.databases;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PreDestroy;

@Slf4j
@Configuration
@EnableMongoRepositories(basePackages = "com.eat.repositories.mongo")
@EnableMongoAuditing
public class MongoConfiguration extends AbstractMongoConfiguration {

    private Mongo mongo;

    @Value(value = "${spring.data.mongodb.name}")
    private String mongoName;

    @Value(value = "${spring.data.mongodb.net.bindIp}")
    private String mongoHost;

    @Value(value = "${spring.data.mongodb.net.port}")
    private int mongoPort;

    @Override
    protected String getDatabaseName() {
        return mongoName;
    }

    @Override
    public Mongo mongo() throws Exception {
        mongo = new MongoClient(mongoHost, mongoPort);
        return mongo;
    }

    @Bean(name = "mongoOperations")
    public MongoOperations mongoOperations() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

    @PreDestroy
    private void destroy() {
        log.info("Successfully closed MongoConnection!");
        mongo.close();
    }

}