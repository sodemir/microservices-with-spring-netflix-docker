 Some endpoints to check config server is working correctly:

     localhost:8888/config/default : you should see the application.properties file in the config repository, as property source

     localhost:8888/producer/default : see application.properties and producer.properties as property sources

     localhost:8888/config/dev : see application-dev.properties in the config repository as property source

     localhost:8888/producer/dev : see application.properties, producer.properties , application-dev.properties, producer-dev.properties as propertysources

     localhost:8888/producer-dev.properties : you should see the list of properties from (application-dev.properties + producer-dev.properties)


 - to switch from git repository to local file repository use

    spring.profiles.active= native
    spring.cloud.config.server.native.searchLocations= classpath:config/

 instead of

    spring.cloud.config.server.git.uri: https://github.com/spring-cloud-samples/config-repo
