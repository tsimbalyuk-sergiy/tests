package dev.tsvinc.tests;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.annotations.NonNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Controller
public final class Client {
  private final RxHttpClient rxHttpClient;
  private final SomeService someService;
  private static final Logger log = LoggerFactory.getLogger(Client.class);

  public Client(RxHttpClient rxHttpClient, SomeService someService) {
    this.rxHttpClient = rxHttpClient;
    this.someService = someService;
  }

  @Get("200")
  public HttpResponse<String> get200() {
    HttpRequest<String> request =
        HttpRequest.GET("http://localhost:8020/200").body("hello").accept(MediaType.ALL);
    HttpResponse<String> stringHttpResponse =
        rxHttpClient.exchange(request, String.class).blockingFirst();
    return HttpResponse.ok("200");
  }

  @Get("200ok")
  public void get200ok() {
    HttpRequest<String> request =
        HttpRequest.GET("http://localhost:8020/200").body("hello").accept(MediaType.ALL);
    rxHttpClient
        .exchange(request, String.class)
        //        .doOnError(throwable -> log.error("error: {}", throwable.getMessage(), throwable))
        //        .doOnNext(stringHttpResponse1 -> log.info("{}", stringHttpResponse1.status()))
        .subscribe(
            o -> {
              log.info("test: {}, {}", o.getStatus().getCode(), o.getStatus().getReason());
            },
            throwable -> {
              log.error("test >> throwable: {}", throwable.getMessage());
            });
    //      ;
  }

  @Get("500")
  public void get500() {
    HttpRequest<String> request =
        HttpRequest.GET("http://localhost:8020/500").body("hello").accept(MediaType.ALL);
    rxHttpClient
        .exchange(request, String.class)
        .doOnNext(stringHttpResponse -> log.info(""))
        .doOnError(throwable -> log.info("500_doOnError: {}", throwable.getMessage()))
        .subscribe(
            o -> log.info("got: {}", o),
            throwable -> log.error("error_in_subscribe: {}", throwable.getMessage()));
  }

  @Get("bad")
  public void getBad() {
    HttpRequest<String> request =
        HttpRequest.GET("http://localhost:4020/500").body("hello").accept(MediaType.ALL);
    rxHttpClient
        .exchange(request, String.class)
        .doOnError(throwable -> log.error("error: {}", throwable.getMessage(), throwable))
        .doOnNext(
            stringHttpResponse1 -> {
              log.info("{}", stringHttpResponse1.status());
              someService.handleError(stringHttpResponse1);
            })
        .subscribe(
            new Subscriber<HttpResponse<String>>() {
              @Override
              public void onSubscribe(@NonNull Subscription s) {
                log.info("sub: {}", s);
              }

              @Override
              public void onNext(HttpResponse<String> stringHttpResponse) {
                log.info("onNext in subscriber: {}", stringHttpResponse);
              }

              @Override
              public void onError(Throwable throwable1) {
                log.error("handle_error: {}", throwable1.getMessage(), throwable1);
              }

              @Override
              public void onComplete() {
                log.info("completed in subscriber");
              }
            });
  }

  @Get("bad2")
  public void getBadReally() {
    final var fakeUrl =
        String.format("http://%s/500", UUID.randomUUID().toString().replace("-", ""));
    HttpRequest<String> request = HttpRequest.GET(fakeUrl).body("hello").accept(MediaType.ALL);
    rxHttpClient
        .exchange(request, String.class)
        .doOnError(throwable -> log.error("error: {}", throwable.getMessage(), throwable))
        .doOnNext(stringHttpResponse1 -> log.info("{}", stringHttpResponse1.status()))
        .subscribe();
  }
}
