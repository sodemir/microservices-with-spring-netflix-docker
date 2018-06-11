 - To see the config server is running correctly, open localhost:8888/producer-service/default
   At this endpoint, you should see the proerties configured for producer-service and base properties for all services

 - to switch from git repository to local file repository use

    spring.profiles.active= native
    spring.cloud.config.server.native.searchLocations= classpath:config/

 instead of

    spring.cloud.config.server.git.uri: https://github.com/spring-cloud-samples/config-repo
