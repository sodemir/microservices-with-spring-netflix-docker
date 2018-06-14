package microservices.spring.forex;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class RemotelyConfiguredController {

    @Value("${eureka.client.serviceUrl.default-zone}")
    private String eurekaUrl;

    @RequestMapping("/eurekaUrl")
    String getEurekaUrl() {
        return this.eurekaUrl;
    }

    @Value("${base.property1}")
    private String baseProperty1;

    @RequestMapping("/baseProperty1")
    String getBaseProperty1() {
        return this.baseProperty1;
    }

    @Value("${base.property2}")
    private String baseProperty2;

    @RequestMapping("/baseProperty2")
    String getBaseProperty2() {
        return this.baseProperty2;
    }
}