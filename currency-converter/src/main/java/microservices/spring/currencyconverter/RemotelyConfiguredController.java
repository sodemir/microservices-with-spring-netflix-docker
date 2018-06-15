package microservices.spring.currencyconverter;


import com.netflix.discovery.converters.Auto;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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