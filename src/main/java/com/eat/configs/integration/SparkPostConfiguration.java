package com.eat.configs.integration;

import com.sparkpost.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkPostConfiguration {

    private final static String API_KEY = "9b3fb735e5862052a26bf39237c8d4223ed6cabc";

    private final static String DOMAIN_MAIL = "noreply@thronefx.com";

    @Bean
    public Client client() {
        Client client = new Client(API_KEY);
        client.setFromEmail(DOMAIN_MAIL);
        return client;
    }

}
