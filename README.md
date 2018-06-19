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

 1. Clone the project
 2. Build all projects using Spring Boot maven plugins using
 
    mwnw install 
    
 3. In the root folder, run 
 
        docker-compose up
        
    This should create and run 1 config-server, 2 eureka and 1 rabbitmq containers.
  
 4. Find out config-server and eureka ports exposed by docker containers 
 
        docker ps
        
 5. You should be able to reach all config-server and eureka servers using these ports 
 
        http://localhost:{port}     
        
 6. You can access RabbitMQ management dashboard via 
 
        http://localhost:15672/#/            
            
 4. Now you can run Forex and Currency-converter jars. ( I didn't put them in docker containers, since as part of the scenario, I assumed, you are supposed to work on them daily. )
 
    Since they are executable jars created buy Spring Boot plugin, all you need is  

        java -jar {jarname}
        
    By default, Forex is running on port 8000 and converter is running on port 8100. You can run multiple Forex services on different ports
     
        java -jar {jarname} -Dserver.port=port_number
         
 5. If everything is fine, both services should be registered to Eureka. Make sure of that.  
 
 6. To check that main business flow is working correctly, go to 
   
        http://localhost:8100/currency-converter/from/USD/to/EUR/quantity/100  
   
        {
            "id": 10001,
            "from": "USD",
            "to": "EUR",
            "conversionMultiple": 0.9,
            "quantity": 100,
            "totalCalculatedAmount": 90,
            "port": 8000
        }
        
    If you run multiple Forex services, the port information in the json should change according to round-robin algorithm.
        

**What it does**

 When you run all services, first of all, Forex and Converter services fetch the configurations from the Config Server. 
 
 Among the configurations is the Eureka server address. Then both will connect to the Eureka and register themselves.
 
 Then, when you call the mentioned money conversion endpoint of the converter service, it will ask Eureka the Forex service address, call it and render the result.  
 

**Docker**

All services have seperate docker files, in the root folder. They are being used by docker-compose. If you want to change anything with the images, you can edit them.

docker-compose.yml is in the project root folder too, if you want to make changes on it. 

_note : All project pom files include docker maven plugin developed by spotify, configured to create images for each microservice. But you don't have to use it._ 
  
**Missing**  

 - eureka multiple çalışacak hale getirilecek. birbirlerini görüp replike olsunlar.
   config server portları statik olacak, o yüzden compose.yml içine 2 tane aynı servisten koyulacak.
   çünkü onu discover edemeyiz, onun portları sabit olmalı
 
 - not tested : multiple forex servers should be notified of config changes
 - automate whole process :
   - build config-server and eureka
   - create docker images and run containers for config-server and eureka
   - build and run forex and currency-converter
 - demonstration of clone and run and test
 - https://stackoverflow.com/questions/50896123/what-is-the-best-practice-to-get-the-file-name-that-was-defined-in-the-pom-xml : 
    
    https://github.com/spotify/docker-maven-plugin/issues/236
    
 - good to have: automated tests that check currency-conversion, config refresh, ribbon and hystrix
 
 - As of now, high availability, resilience, fault tolerance, service orchestration aspects of the are lacking. Require enhancements on these topics.

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


Best practices Questions :
 - should i put actively developed projects in docker images, in my dev environment?
 