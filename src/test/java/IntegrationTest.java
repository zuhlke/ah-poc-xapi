import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

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

        assertThat(responseText, equalTo("This is a springboot app"));
    }

    @Test
    public void GET_balances_returnsBalancesFromPapi() throws UnirestException {
        String expectedJson = "{\"balances\":\"stubbed\"}";
        PapiService stubPapiService = mock(PapiService.class);
        when(stubPapiService.balancesJson("1")).thenReturn(expectedJson);
        SpringController.startWithInjectedDependencies(portNumber, stubPapiService);

        String responseText = getRequestText("/balances?customer-id=1");

        assertThat(responseText, equalTo(expectedJson));
    }

    private String getRequestText(String requestPath) throws UnirestException {
        String url = origin + requestPath;
        HttpResponse<String> response = Unirest.get(url).asString();
        return response.getBody();
    }
}
