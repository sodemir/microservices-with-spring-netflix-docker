package microservices.spring.currencyconverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FeignForexServiceProxy forexServiceProxy;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
                                                  @PathVariable BigDecimal quantity) {


        CurrencyConversionBean response = forexServiceProxy.exchangeRate(from, to);

        return new CurrencyConversionBean(
                response.getId(), from, to,
                response.getConversionMultiple(), quantity,
                quantity.multiply(response.getConversionMultiple()),
                response.getPort());
    }

}
