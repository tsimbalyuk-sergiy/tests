package dev.tsvinc.tests;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Controller
public final class RestController {
  private static final Logger log = LoggerFactory.getLogger(RestController.class);

  private static final String _200 = "200";

  @Get("/200")
  public HttpResponse<String> ok200(HttpRequest<?> request) {
    log.info("{}", request);
    return HttpResponse.ok(_200).header("hello", "dude");
  }

  @Get("/500")
  public MutableHttpResponse<Object> nope500(HttpRequest<?> request) {
    log.info("{}", request);
    return HttpResponse.serverError().header("id", UUID.randomUUID().toString());
  }
}
