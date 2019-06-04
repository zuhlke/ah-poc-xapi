public class SumBalanceResponseBody {
    private final double sumBalance;
    private final String customerId;

    public SumBalanceResponseBody(String customerId, double sumBalance) {
        this.customerId = customerId;
        this.sumBalance = sumBalance;
    }

    public String toJson() {
        return "{\"accountNumber\": \"" + customerId + "\", \"sumBalance\": \"" + sumBalance + "\"}";
    }
}
