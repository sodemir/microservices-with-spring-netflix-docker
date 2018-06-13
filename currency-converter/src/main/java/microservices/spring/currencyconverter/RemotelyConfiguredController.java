package microservices.spring.currencyconverter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class RemotelyConfiguredController {

    @Value("${message:default message}")
    private String message;

    @Value("${something}")
    private String something;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }

    @RequestMapping("/something")
    String getSeomthing() {
        return this.something;
    }
}