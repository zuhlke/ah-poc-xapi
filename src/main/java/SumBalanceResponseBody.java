public class SumBalanceResponseBody {
    private final double sumBalance;

    public SumBalanceResponseBody(double sumBalance) {
        this.sumBalance = sumBalance;
    }

    public String toJson() {
        return "{\"sumBalance\": \"" + sumBalance + "\"}";
    }
}
