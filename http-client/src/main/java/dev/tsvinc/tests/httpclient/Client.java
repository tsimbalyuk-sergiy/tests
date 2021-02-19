package dev.tsvinc.tests.httpclient;

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

import java.net.URI;
import java.net.http.HttpClient;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public final class Client {
  private final RxHttpClient rxHttpClient;
  private final SomeService someService;
  private static final Logger log = LoggerFactory.getLogger(Client.class);

  public Client(RxHttpClient rxHttpClient, SomeService someService, HttpClient httpsClient) {
    this.rxHttpClient = rxHttpClient;
    this.someService = someService;
    this.httpsClient = httpsClient;
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

  //  private static final HttpClient httpClient =
  //      HttpClient.newBuilder()
  //          .version(HttpClient.Version.HTTP_2)
  //          .connectTimeout(Duration.ofSeconds(10))
  //          .build();
  private final java.net.http.HttpClient httpsClient;

  @Get("500ahc")
  public void get500Ahc() throws InterruptedException, ExecutionException, TimeoutException {

    java.net.http.HttpRequest request =
        java.net.http.HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("http://localhost:8020/500"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .build();

    Void response =
        this.httpsClient
            .sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
            .exceptionally(
                throwable -> {
                  log.error("test >> throwable: {}", throwable.getMessage());
                  return null;
                })
            .thenAccept(httpResponse -> log.info("@@@{}", httpResponse.statusCode()))
            .get(5, TimeUnit.SECONDS);
  }

  @Get("500")
  public void get500() {
    HttpRequest<String> request =
        HttpRequest.GET("http://localhost:8020/500").body("hello").accept(MediaType.ALL);
    final var subscribe =
        rxHttpClient
            .exchange(request, String.class)
            .singleOrError()
            .doOnSuccess(
                stringHttpResponse -> {
                  log.info(
                      "test: {}, {}",
                      stringHttpResponse.getStatus().getCode(),
                      stringHttpResponse.getStatus().getReason());
                })
            .doOnError(
                throwable -> {
                  log.error("test >> throwable: {}", throwable.getMessage());
                })
            .subscribe((stringHttpResponse, throwable) -> {});

    /*.subscribeWith(
    new DisposableSingleObserver<HttpResponse<String>>() {
      @Override
      public void onSuccess(@NonNull HttpResponse<String> stringHttpResponse) {
        log.info(
            "test: {}, {}",
            stringHttpResponse.getStatus().getCode(),
            stringHttpResponse.getStatus().getReason());
      }

      @Override
      public void onError(@NonNull Throwable e) {
        log.error("test >> throwable: {}", e.getMessage());
      }
    })*/ ;
    /*.subscribe(
    (stringHttpResponse, throwable) -> {
      if (null != throwable) {
        log.error("test >> throwable: {}", throwable.getMessage());
      } else {
        log.info(
            "test: {}, {}",
            stringHttpResponse.getStatus().getCode(),
            stringHttpResponse.getStatus().getReason());
      }
    });*/

    //                o -> {
    //                  log.info("test: {}, {}", o.getStatus().getCode(),
    // o.getStatus().getReason());
    //                },
    //                throwable -> {
    //                  log.error("test >> throwable: {}", throwable.getMessage());
    //                },
    //                () -> log.info("finished"));
    /*while (!subscribe.isDisposed()) {
      if (subscribe.isDisposed()) {
        log.info("subscribe disposed: {}", LocalDateTime.now());
      } else {
        log.info("subscribe not disposed: {}", LocalDateTime.now());
      }
    }*/
    //            .doOnSuccess(o -> log.info("got: {}", o))
    //            .doOnError(throwable -> log.info("500_doOnError: {}", throwable.getMessage()));
    //    if (!subscribe.isDisposed()) {
    //      subscribe.dispose();
    //    }

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
