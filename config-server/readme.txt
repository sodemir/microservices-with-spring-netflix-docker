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


 - to run rabbitmq

  docker pull rabbitmq:3-management
  docker run --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management

  access rabbitmq dashboard: http://localhost:15672/#/

  trigger manuel refresh : http --form POST :8888/monitor path=forex  (will refresh forex default properties)

--------
 - good read : http://www.enriquerecarte.com/2017-08-04/spring-cloud-config-series-git-backend

**Detecting Configuration Changes**
We already saw a quick overview of how you can refresh your application’s context in the previous post, but in this section we’ll take a look at an example with a real implementation of how this could be done.

Spring Cloud provides a solution to detecting configuration changes in your applications based on a couple of libraries:

spring-cloud-config-monitor: This library adds an endpoint to your application which understands webhooks from a few different Git server providers such as Github or Bitbucket. If you include this dependency, it will add a /monitor endpoint. You then just need to configure the webhook in the Git repository to point wherever this endpoint is deployed. Once this endpoint is called it will parse the payload to understand what has changed, and it will then use the next library to tell the client applications that there is a change in configuration, so they should refresh the application context. You can find out more information about this library in this blog post.
spring-cloud-bus: This is a simple library that Spring provides to communicate global events to other applications through some message brokers. It’s mostly used for this use case, where you need to send an RefreshRemoteApplicationEvent event to all/some applications telling them that they should refresh their application context. It integrates with either RabbitMQ or Kafka as the message brokers.
Let’s take a look at an architectural overview of the solution:

Standalone Config Setup With Refresh

There are a few changes from the previous diagrams we were looking at:

The Server Application now has the Spring Cloud Config Monitor component which will listen for requests to POST /monitor.
There is a RabbitMQ broker now.
The Client Application also integrates with Spring Cloud Bus, which adds a RefreshListener which will listen for RefreshRemoteApplicationEvent. If this event happens, it will trigger a context refresh the same way the RefreshEndpoint does.
In this example, I’ve only considered the standalone setup discussed earlier. For the embedded setup, you would have to have a custom solution since there is no centralized server that will manage all the configuration to which you can add the /monitor endpoint.
