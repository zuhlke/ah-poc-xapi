import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

public class ReactiveRestClient {
    private final WebClient webClient;

    public ReactiveRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseSpec get(String url) {
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.error(new Exception("Request 'GET " + url + "' gave response with status code" + response.statusCode().value())));
    }
}
