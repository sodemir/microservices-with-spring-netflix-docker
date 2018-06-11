# microservices-with-spring-netflix-docker
Template project with spring boot, eureka, zuul, spring config server, docker

1. This project is coded referencing this link : http://www.springboottutorial.com/creating-microservices-with-spring-boot-part-1-getting-started
2. It is a spring boot, microservice, feign, ribbon, eureka, zuul, spring cloud config sample. Using H2 as database.
3. Forex microservice is returning conversion rates. Currency-converter microservice is returning actual money amount, calling Forex service during the process
4. Feing is used to call forex rest service
5. Ribbon is used for client-side load balancing. Forex service is load balanced, while being called from currency-converter.
6. Eureka is used as service registry. It is working as a standalone service. 
   Forex and Converter services are registered to Eureka, so that, they can be discovered without depending on their actual ip and port numbers.
   When Eureka is used, the caller needs to know the name of the service. Eureka routes to the actual ip and ports.
   The name of the service is assigned in the feignclient class. There is a value on ribbon annotation too, but it doesnt seem to effect the result.  
7. How does Ribbon + Eureka interact? 
	The feign client in the consumer service (currency-converter) fetches the url(s) registered to the registered services. 
	Then, the activated Ribbon in the currency-converter service, will visit these multiple urls in round-robin fashion. 
8. A service registered to Eureka will be deregistered when it is properly shutdown (actuator/shutdown). Its status will be DOWN, when /actuator/health is not OK
9. Hystrix is introduced for fault tolerancy of forex service. When forex service is not available, hystrix fallbacks to local solution. 
   It will open the circuit, if the error threshold (configured as 3 here) is reached. And will reset the circuit after sometime(not configured, using the default).
   See FeignClient as the hystrix fallback.
10. Redis is used as the cache [missing]
11. Spring cloud config library is used to pull config values from a single point. 
	There are two ways to use this library; 1. as a standalone server or an embedded server (see https://dzone.com/articles/spring-cloud-config-series-part-2-git-backend for further details)
	Here, I used it as a standalone server. 
	To see the config server is working properly, go to localhost:8888/forex/default

	To refresh values from the server, when there is a change, 
	
 and cloud bus is used to propogate changes in the configurations. RabbitMQ (in a docker image) is used as underlying message queue
    To run the rabbitmq image : docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
	To connect to management dashboard : localhost:15672
	default user/pwd : guest/guest


   
