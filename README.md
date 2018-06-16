# microservices-with-spring-netflix-docker

Template project demonstrating microservices arhictecture with spring boot, spring cloud, eureka, zuul, spring config server, hystrix, ribbon, feign and docker. 

There are **5 microservices** included in this project:

 1. Forex Service - Returns the exchange values between monetary units. See forex/readme.md for further details.
 2. Currency Converter Service - Returns the conversion amount between two monetary units. Calls Forex Service to get the exchange value. See currency-converter/readme.md for further details.  
 3. Spring Cloud Config Server - Being used to centralize the configuration of Forex and Currency-Converter services. See config-server/readme.md for further details.
   _config-repository_ folder is for keeping configuration files of these two microservices.  
 4. Eureka Server - Being used as service registry. (The config server keeps the url of eureka, other services see eureka through config server, not vice versa. So, config-server urls are hardcoded)
 5. Zuul - Gateway service

**How to install and run**

 - Clone the project.
 - Create image files for all services by either running the mvn docker plugins or using docker build.
 - You need a RabbitMQ service for configuration auto refresh

        docker pull rabbitmq:3-management   
        docker run --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
        access rabbitmq dashboard: http://localhost:15672/#/
  
 - Run the services in this order : rabbitmq, config server, eureka, forex/currency-converter 
 - To check all main flow is working correctly, go to 
   
        {currency converter service ip:port}/currency-converter/from/USD/to/EUR/quantity/100  
   
        {
        "id": 10001,
        "from": "USD",
        "to": "EUR",
        "conversionMultiple": 0.9,
        "quantity": 100,
        "totalCalculatedAmount": 90,
        "port": 8000
        }

**What id does**

 When you run all services, firts of all, Forex and Converter services fetch the configurations from the config server. 
 Among them is the eureka server address. Both will connect to the Eureka and register themselves.
 
 Then, when you call the mentioned conversion endpoint of the converter service, it will ask Eureka the Forex service address, call it and render the result.  
 


**Docker**

All project pom files have docker maven plugins, configured to create images for each microservice. All services have seperate dockerfiles.

To create a docker image for a microservice, run _mvnw install dockerfile:build_
  
**Missing**  

1. As of now, high availability, resilience, fault tolerance, service orchestration aspects of the are lacking. Require enhancements on these topics.

    Never the less, you can run multiple instances of all services and system works properly. This gives some availability and resiliance to some degree. 
Also, calls between converter and forex has fault tolerance; if forex is down, the converter will use cached data instead.

2.  
1. Start of this project is this link : http://www.springboottutorial.com/creating-microservices-with-spring-boot-part-1-getting-started
3. Forex microservice is returning conversion rates. Currency-converter microservice is returning actual money amount, calling Forex service during the process
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


   

some resources :
 - spring.io/guides
 - http://www.springboottutorial.com/creating-microservices-with-spring-boot-part-1-getting-started
 - dzone.com/articles/buiding-microservice-using-spring-boot-and-docker
 - https://dzone.com/articles/circuit-breaker-fallback-and-load-balancing-with-n
 - https://spencer.gibb.us/blog/2015/09/24/spring-cloud-config-push-notifications/
 - http://www.baeldung.com/spring-cloud-configuration
 - http://www.enriquerecarte.com/2017-08-04/spring-cloud-config-series-git-backend
 - http://www.baeldung.com/spring-cloud-netflix-hystrix
