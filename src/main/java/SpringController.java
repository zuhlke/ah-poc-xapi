import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@EnableAutoConfiguration
public class SpringController {
    private static ConfigurableApplicationContext context;
    private static RequestHandler requestHandler;

    public static void start(int portNumber) {
        SpringProps props = SpringProps.constructDefaultProps(portNumber);
        SpringController.requestHandler = new RequestHandler(new ReactivePapiService(new WebfluxRestClient(WebClient.create()), props.papiBalancesUrlTemplate));
        context = new SpringApplicationBuilder(SpringController.class)
                .properties(props.toMap())
                .run();
    }

    public static void startWithInjectedReactiveRestClient(int portNumber, ReactiveRestClient reactiveRestClient) {
        SpringProps props = SpringProps.constructDefaultProps(portNumber);
        SpringController.requestHandler = new RequestHandler(new ReactivePapiService(reactiveRestClient, props.papiBalancesUrlTemplate));
        context = new SpringApplicationBuilder(SpringController.class)
                .properties(props.toMap())
                .run();
    }

    public static void stop() {
        int exitCode = 0;
        SpringApplication.exit(context, (ExitCodeGenerator) () -> exitCode);
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
