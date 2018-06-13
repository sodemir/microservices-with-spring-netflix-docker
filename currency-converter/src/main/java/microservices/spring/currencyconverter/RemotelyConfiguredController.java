package microservices.spring.currencyconverter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class RemotelyConfiguredController {

    @Value("${converter.property}")
    private String converterProperty;

    @RequestMapping("/converterProperty")
    String getMessage() {
        return this.converterProperty;
    }
}