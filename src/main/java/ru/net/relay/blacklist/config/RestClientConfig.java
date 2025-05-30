package ru.net.relay.blacklist.config;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean("antifilter.download")
    public RestClient getEntifilterRestClient() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().disableContentCompression().build());

        return RestClient.builder()
                .requestFactory(httpComponentsClientHttpRequestFactory)
                .baseUrl("https://antifilter.download")
                .build();
    }

    @Bean("google")
    public RestClient getGoogleRestClient() {
        return RestClient.builder()
                .baseUrl("https://www.gstatic.com/")
                .build();
    }

    @Bean("bgpview")
    public RestClient getBgpviewRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.bgpview.io")
                .build();
    }
}
