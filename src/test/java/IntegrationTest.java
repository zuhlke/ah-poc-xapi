import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntegrationTest {
    private final int portNumber = 9090;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @After
    public void tearDown() {
        SpringController.stop();
    }

    @Test
    public void GET_slash_returnsHelloMessage() throws UnirestException {
        SpringController.start(portNumber);

        String responseText = getRequestText("/");

        assertThat(responseText, equalTo("This is the aimless-hammer PoC XAPI."));
    }

    @Test
    public void GET_sumBalances_returnsSumOfBalancesFromPapi() throws UnirestException, IOException {
        ReactiveRestClient stubReactiveRestClient = mock(ReactiveRestClient.class);
        when(stubReactiveRestClient.get("https://ah-poc-papi-springboot.cfapps.io/reactive-balance?customer-id=1", BalanceInformation[].class)).thenReturn(Mono.just(new BalanceInformation[]{
                BalanceInformation.fromFields("CreditCardAccount", "1234567890", "1234.50"),
                BalanceInformation.fromFields("CurrentAccount", "64746383648", "34.50")
        }));
        SpringController.startWithInjectedReactiveRestClient(portNumber, stubReactiveRestClient);

        String responseText = getRequestText("/sum-balances?customer-id=1");

        Map<String, Double> map = readSumBalanceJsonResponseToMap(responseText);
        assertThat(map.get("sumBalance"), equalTo(1269.0));
    }

    private Map<String, Double> readSumBalanceJsonResponseToMap(String responseText) throws IOException {
        return objectMapper.readValue(responseText, new TypeReference<Map<String, Double>>() {
        });
    }

    private String getRequestText(String requestPath) throws UnirestException {
        String origin = "http://localhost:" + portNumber;
        String url = origin + requestPath;
        HttpResponse<String> response = Unirest.get(url).asString();
        return response.getBody();
    }
}
