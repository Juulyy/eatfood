package com.eat.configs.databases;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetSocketAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.eat.repositories.elasticsearch")
public class ElasticsearchConfiguration {

    public static final String SERVER_IP = "localhost";
    public static final String INDEX_NAME = "eat";
    public static final String PLACES_TYPE_NAME = "places";
    public static final String USERS_TYPE_NAME = "users";


    @Value(value = "${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Value(value = "${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        String server = clusterNodes.split(":")[0];
        Integer port = Integer.parseInt(clusterNodes.split(":")[1]);
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", clusterName).build();
        TransportClient client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(server, port)));
        return new ElasticsearchTemplate(client);
    }

}
