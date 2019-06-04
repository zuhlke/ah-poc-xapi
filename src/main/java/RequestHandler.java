public class RequestHandler {
    private PapiService papiService;

    public RequestHandler(PapiService papiService) {
        this.papiService = papiService;
    }

    public String home() {
        return "This is a springboot app";
    }

    public String balances(String customerId) {
        return papiService.balancesJson(customerId);
    }
}
