package dev.tsvinc.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Flowable;

@Controller
public final class Client {
    private final RxHttpClient rxHttpClient;
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public Client(RxHttpClient rxHttpClient) {
        this.rxHttpClient = rxHttpClient;
    }

    @Get("200")
    public HttpResponse<String> get200(){
        HttpRequest request = HttpRequest.GET("http://localhost:8020/200").accept(MediaType.ALL);
        HttpResponse<String> flowable = rxHttpClient.exchange(request, String.class).blockingFirst();
        log.info("{}", flowable);
        return HttpResponse.ok("200");
    }
}
