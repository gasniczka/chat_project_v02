# chatv2 Project


----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 
user manual


1) przygotuj strukture katalogów w katalogu głownym 

`/chatClientFiles/`
`/chatServerFiles/`


2) uruchom 

a) activemq artemis 
z wiersza polecen     `docker run -it --rm -p 8161:8161 -p 61616:61616 -p 5672:5672 -e AMQ_USER=quarkus -e AMQ_PASSWORD=quarkus quay.io/artemiscloud/activemq-artemis-broker:0.1.4`
lub z     `docker-compose.yml`

b) server chatu - ChatV2

c) klienta chatu - client


po uruchomieniu klienta i wpisaniu w konsoli `//?` wyświetli się menu chatu

wpisanie dowolnego ciągu znaku spowoduje wysłanie wiadomości do wszystkich klientów oraz servera jako jednostki zarządzającej 


----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 

dostęp do konsoli artemis
`http://127.0.0.1:8161/console`
user/pass quarkus/quarkus

topiki definiowane przez consumera i producera


endpoint restowy do przykładu
`http://localhost:8080/hello`

dwa endpointy do obsługi upload i download plików wymagają MultipartBody
`http://localhost:8080/files/upload`
`http://localhost:8080/files/download` 


dostęp do konsoli H2
`http://localhost:8080/h2`


----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 
inne

guide do jms   https://quarkus.io/guides/jms
https://activemq.apache.org/components/artemis/documentation/latest/management-console.html
https://activemq.apache.org/components/artemis/documentation/latest/using-jms.html






This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/chatv2-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- AMQP 1.0 JMS client - Apache Qpid JMS ([guide](https://quarkus.io/guides/jms)): Use JMS APIs with AMQP 1.0 servers
  such as ActiveMQ Artemis, ActiveMQ 5, Qpid Broker-J, Qpid Dispatch router, Azure Service Bus, and more
- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and
  JPA
- Quarkus Extension for Spring Data REST ([guide](https://quarkus.io/guides/spring-data-rest)): Generate JAX-RS
  resources for a Spring Data application
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing JAX-RS and more
- Quarkus Extension for Spring Data JPA API ([guide](https://quarkus.io/guides/spring-data-jpa)): Use Spring Data JPA
  annotations to create your data access layer

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
