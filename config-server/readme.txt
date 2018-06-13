- Some endpoints to check config server is working correctly:

     localhost:8888/config/default : you should see the application.properties file in the config repository, as property source

     localhost:8888/producer/default : see application.properties and producer.properties as property sources

     localhost:8888/config/dev : see application-dev.properties in the config repository as property source

     localhost:8888/producer/dev : see application.properties, producer.properties , application-dev.properties, producer-dev.properties as propertysources

     localhost:8888/producer-dev.properties : you should see the list of properties from (application-dev.properties + producer-dev.properties)


- to create docker image : mvnw install dockerfile:build

 - to switch from git repository to local file repository use

    spring.profiles.active= native
    spring.cloud.config.server.native.searchLocations= classpath:config/

 instead of

    spring.cloud.config.server.git.uri: https://github.com/spring-cloud-samples/config-repo


 - good read : http://www.enriquerecarte.com/2017-08-04/spring-cloud-config-series-git-backend

 - to configure auto refresh :

   download and run rabbitmq image

   docker pull rabbitmq:3-management
   docker run --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management