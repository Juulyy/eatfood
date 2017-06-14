package com.eat.services.elasticsearch.client;

import lombok.Getter;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ESClient {

    @Getter
    private TransportClient client = null;

    public ESClient() {
        /*Settings settings = Settings.builder()
                .put("cluster.name", ElasticsearchConfiguration.CLUSTER_NAME).build();
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(
                            InetAddress.getByName(ElasticsearchConfiguration.SERVER_IP),
                            ElasticsearchConfiguration.SERVER_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/
    }
}
