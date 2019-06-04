import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class ReactivePapiService implements PapiService {
    private final WebClient webClient = WebClient.create();
    private final String balancesPapiUrlTemplate;

    public ReactivePapiService(String balancesPapiUrlTemplate) {
        this.balancesPapiUrlTemplate = balancesPapiUrlTemplate;
    }

    @Override
    public Mono<Double> sumBalances(String customerId) {
        String url = expandUrl(balancesPapiUrlTemplate, customerId);

        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.error(new Exception("Papi returned status " + response.statusCode().value())))
                .bodyToMono(BalanceInformation[].class)
                .map(Arrays::asList)
                .map(List::stream)
                .map(balanceInformationStream -> balanceInformationStream.mapToDouble(balanceInformation -> Double.parseDouble(balanceInformation.getBalance())).sum());
    }

    private String expandUrl(String template, Object... values) {
        UriTemplate uriTemplate = new UriTemplate(template);
        return uriTemplate.expand(values).toString();
    }
}
