package com.mobei.es.api.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                //如果是集群就配多个
                RestClient.builder(
                        new HttpHost("192.168.222.4", 9200, "http")/*,
                        new HttpHost("localhost", 9201, "http")*/));
        return client;
    }
}
