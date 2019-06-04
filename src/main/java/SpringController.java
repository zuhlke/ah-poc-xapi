import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RestController
@EnableAutoConfiguration
public class SpringController {
    private static ConfigurableApplicationContext context;
    private static RequestHandler requestHandler;

    public static void start(int portNumber) {
        HashMap<String, Object> props = createProps(portNumber);
        SpringController.requestHandler = new RequestHandler(new ReactivePapiService((String) props.get("papi.balances.url")));
        context = new SpringApplicationBuilder(SpringController.class)
                .properties(props)
                .run();
    }

    public static void startWithInjectedPapiService(int portNumber, PapiService papiService) {
        HashMap<String, Object> props = createProps(portNumber);
        SpringController.requestHandler = new RequestHandler(papiService);
        context = new SpringApplicationBuilder(SpringController.class)
                .properties(props)
                .run();
    }

    public static void stop() {
        int exitCode = 0;
        SpringApplication.exit(context, (ExitCodeGenerator) () -> exitCode);
    }

    private static HashMap<String, Object> createProps(int portNumber) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", portNumber);
        props.put("papi.balances.url", "https://ah-poc-papi-springboot.cfapps.io/reactive-balance?customer-id={CUSTOMER_ID}");
        return props;
    }

    @RequestMapping("/")
    public String home() {
        return requestHandler.home();
    }

    @RequestMapping("/sum-balances")
    public Mono<String> balances(@RequestParam("customer-id") String customerId) {
        return requestHandler.sumBalances(customerId);
    }
}
