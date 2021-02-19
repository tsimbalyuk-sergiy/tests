package dev.tsvinc.tests.httpclient;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;

import java.net.http.HttpClient;
import java.time.Duration;

@Factory
public class Cfg {
  @Value("${app.timeout}")
  private int timeout;

  @Bean
  public HttpClient httpClient() {
    return HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(this.timeout)).build();
  }
}
