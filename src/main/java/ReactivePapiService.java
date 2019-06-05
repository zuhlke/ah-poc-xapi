import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class ReactivePapiService implements PapiService {
    private final ReactiveRestClient reactiveRestClient;
    private final String balancesPapiUrlTemplate;

    public ReactivePapiService(ReactiveRestClient reactiveRestClient, String balancesPapiUrlTemplate) {
        this.reactiveRestClient = reactiveRestClient;
        this.balancesPapiUrlTemplate = balancesPapiUrlTemplate;
    }

    @Override
    public Mono<Double> sumBalances(String customerId) {
        String url = expandUrl(balancesPapiUrlTemplate, customerId);

        return reactiveRestClient.get(url)
                .bodyToMono(BalanceInformation[].class)
                .map(balanceInformationArray -> Arrays.stream(balanceInformationArray)
                        .mapToDouble(BalanceInformation::balance)
                        .sum());
    }

    private String expandUrl(String template, Object... values) {
        return new UriTemplate(template).expand(values).toString();
    }
}
