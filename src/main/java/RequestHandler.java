import reactor.core.publisher.Mono;

public class RequestHandler {
    private final PapiService papiService;

    public RequestHandler(PapiService papiService) {
        this.papiService = papiService;
    }

    public String home() {
        return "This is the aimless-hammer PoC XAPI.";
    }

    public Mono<String> sumBalances(String customerId) {
        return papiService.sumBalances(customerId).map(sumBalance -> new SumBalanceResponseBody(customerId, sumBalance).toJson());
    }
}
