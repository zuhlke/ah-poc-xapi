import reactor.core.publisher.Mono;

public interface ReactiveRestClient {
    <T> Mono<T> get(String url, Class<T> bodyType);
}
