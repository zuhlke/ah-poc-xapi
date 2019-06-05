import au.com.dius.pact.consumer.ConsumerPactTestMk2;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReactivePapiServicePact extends ConsumerPactTestMk2 {
    @Override
    protected RequestResponsePact createPact(PactDslWithProvider builder) {
        /*
        This section of code is used by pact to produce the main body of the pact file.
        The stubbed behaviour defined here is the standard that the provider, which this code imitates, is held to by the pact.
        */

        String stubbedResponseBody = "[{\"accountType\": \"CreditCardAccount\",\"accountNumber\": \"1234567890\",\"balance\": \"1234.50\"},{\"accountType\": \"CurrentAccount\",\"accountNumber\": \"64746383648\",\"balance\": \"34.50\"}]";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return builder
                .given("The PAPI is backed by two working SAPIs, each able to serve account data for a customer with id 1")
                .uponReceiving("A request for a list of balances for a customer with id 1")
                .path("/reactive-balance")
                .query("customer-id=1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(stubbedResponseBody)
                .toPact();
    }

    @Override
    protected void runTest(MockServer mockServer) {
        int port = mockServer.getPort();
        String stubPapiOrigin = "http://localhost:" + port;
        WebfluxRestClient webfluxRestClient = new WebfluxRestClient(WebClient.create());
        ReactivePapiService reactivePapiService = new ReactivePapiService(webfluxRestClient, stubPapiOrigin + "/reactive-balance?customer-id={CUSTOMER_ID}");

        assertThatItCanSumAccountBalances(reactivePapiService);
        /*
        For testing more interactions with the PAPI, I believe you would put more assertions here. That is at least what I've seen from the working example.

        Working Example: https://github.com/zuhlke/pact-consumer/blob/master/src/test/java/com/zuhlke/report/service/ConsumerTest.java
        */
    }

    private void assertThatItCanSumAccountBalances(ReactivePapiService reactivePapiService) {
        Mono<Double> sumBalances = reactivePapiService.sumBalances("1");

        assertThat(sumBalances.block(), equalTo(1269.0));
    }

    @Override
    protected String providerName() {
        return "ah-poc-papi-springboot";
    }

    @Override
    protected String consumerName() {
        return "ah-poc-xapi";
    }
}