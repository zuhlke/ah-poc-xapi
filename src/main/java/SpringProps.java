import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class SpringProps {
    public final String papiBalancesUrlTemplate;
    public final int portNumber;

    private SpringProps(int portNumber, String papiBalancesUrlTemplate) {
        this.portNumber = portNumber;
        this.papiBalancesUrlTemplate = papiBalancesUrlTemplate;
    }

    public static SpringProps constructDefaultProps(int portNumber) {
        return new SpringProps(
                portNumber,
                "https://ah-poc-papi-springboot.cfapps.io/reactive-balance?customer-id={CUSTOMER_ID}"
        );
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", portNumber);
        props.put("papi.balances.url", papiBalancesUrlTemplate);
        return props;
    }
}
