package microservices.spring.currencyconverter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name="forex-service", fallback = FeignForexServiceProxy.ForexServiceProxyFallback.class)
public interface FeignForexServiceProxy {

    @GetMapping("/exchange-rate/from/{from}/to/{to}")
    public CurrencyConversionBean exchangeRate(@PathVariable("from") String from, @PathVariable("to") String to);


    @Component
    class ForexServiceProxyFallback implements FeignForexServiceProxy{

        @Override
        public CurrencyConversionBean exchangeRate(String from, String to) {
            return new CurrencyConversionBean((long) 1, from, to, BigDecimal.TEN, null, null, 0);
        }
    }

}
