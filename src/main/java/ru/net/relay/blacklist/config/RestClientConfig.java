package ru.net.relay.blacklist.config;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient getRestClient() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().disableContentCompression().build());

        return RestClient.builder()
                .requestFactory(httpComponentsClientHttpRequestFactory)
                .baseUrl("https://antifilter.download")
                .build();
    }
}
