import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebfluxRestClient implements ReactiveRestClient {
    private final WebClient webClient;

    public WebfluxRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public <T> Mono<T> get(String url, Class<T> bodyType) {
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.error(new Exception("Request 'GET " + url + "' gave response with status code" + response.statusCode().value())))
                .bodyToMono(bodyType);
    }
}
