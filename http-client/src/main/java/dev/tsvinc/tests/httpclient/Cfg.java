package dev.tsvinc.tests.httpclient;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import java.net.http.HttpClient;
import java.time.Duration;

@Factory
public class Cfg {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    }
}
