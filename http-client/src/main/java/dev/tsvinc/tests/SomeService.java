package dev.tsvinc.tests;

import io.micronaut.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class SomeService {
  private static final Logger log = LoggerFactory.getLogger(SomeService.class);

  public void handleError(HttpResponse<String> httpResponse) {
    log.info(
        "handling somehow ... body: {}, code: {}, type: {}, headers: {}",
        httpResponse.getBody(),
        httpResponse.getStatus().getCode(),
        httpResponse.getContentType(),
        httpResponse.getHeaders());
  }
}
