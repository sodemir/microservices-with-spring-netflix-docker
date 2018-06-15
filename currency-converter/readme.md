What It Does
-

converts from one money unit to another. retrieves the exchange-value from forex service. 

Available Endpoints
-

**conversion endpoint** : _/currency-converter/from/{from}/to/{to}/quantity/{quantity}_


list of {from} and {to} is defined in forex service.

sample : /currency-converter/from/USD/to/EUR/quantity/10

**to see configuration is OK :** _/converterProperty_ 

this endpoint should return the correct value from config server.

Important Libraries Used
--
 - Using **Spring Config Client** to connect to **Spring Cloud Config Server** and load the configuration properties during bootstrap.   
 - Using **Eureka** for service discovery. Also, registering itself to Eureka.
 - Using **Feign** to create a client proxy for http call o the Forex service.
 - Using **Ribbon** for client side load balancing. FeignProxy is annotated as a RibbonClient. So, Spring creates a RibbonProxy while calling the Forex service.
   For example, when u run multiple Forex services (and they are registered to Eureka), Ribbon will round-robbin these services.
 - **Hystrix** is enabled for fault tolerance. So, if no Forex service is available (or there is an error while calling), 
   then Hystrix will call fallback method. This fallback method is calling redis-cached data.
   
   Hystrix dashboard is also enabled.
