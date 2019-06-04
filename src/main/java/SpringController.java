import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@EnableAutoConfiguration
public class SpringController {
    private static ConfigurableApplicationContext context;

    private static RequestHandler requestHandler = new RequestHandler(new ReactivePapiService());

    public static void start(int portNumber) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", portNumber);
        context = new SpringApplicationBuilder(SpringController.class)
                .properties(props)
                .run();
    }

    public static void startWithInjectedDependencies(int portNumber, PapiService papiService) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", portNumber);
        SpringController.requestHandler = new RequestHandler(papiService);
        context = new SpringApplicationBuilder(SpringController.class)
                .properties(props)
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

    @RequestMapping("/balances")
    public String balances(@RequestParam("customer-id") String customerId) {
        return requestHandler.balances(customerId);
    }
}
