import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Test;
import reactor.core.publisher.Mono;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntegrationTest {
    private final int portNumber = 9090;
    private final String origin = "http://localhost:" + portNumber;

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
    public void GET_sumBalances_returnsSumOfBalancesFromPapi() throws UnirestException {
        PapiService stubPapiService = mock(PapiService.class);
        when(stubPapiService.sumBalances("1")).thenReturn(Mono.just(1234.0));
        SpringController.startWithInjectedPapiService(portNumber, stubPapiService);

        String responseText = getRequestText("/sum-balances?customer-id=1");

        String expectedResponse = "{\"accountNumber\": \"1\", \"sumBalance\": \"1234.0\"}";
        assertThat(responseText, equalTo(expectedResponse));
    }

    private String getRequestText(String requestPath) throws UnirestException {
        String url = origin + requestPath;
        HttpResponse<String> response = Unirest.get(url).asString();
        return response.getBody();
    }
}
