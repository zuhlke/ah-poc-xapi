import reactor.core.publisher.Mono;

public interface PapiService {
    Mono<Double> sumBalances(String customerId);
}
