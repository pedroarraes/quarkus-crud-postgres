podman exec -it postgresql_database bash

psql

\c crud

select * from person;

# How to create a CRUD application using Quarkus Native + PostgreSQL Database
This smart start demonstrates the utilization of a PostgreSQL Database for creating a Quarkus native cloud application. The project leverages Quarkus, a high-performance Java framework known as the Supersonic Subatomic Java Framework. To explore further details about Quarkus, please visit its official website: https://quarkus.io/."

## Requeriments
* OpenJDK 17
* Apache Maven 3.8.5
* VSCode or any IDE maven supportable
* Postgresql 15
* podman 4.5.1


## Summary
* [Staring PostgreSQL](#staring-postgreSQL)
    * [Obtaining PostgreSQL Red Hat image](#obtaining-postgresql-red-hat-image)
    * [Running container](#running-container)
* [Executing the application in development mode](#executing-the-application-in-development-mode)
* [Testing API application](#testing-api-application)
* [Validating Data]()
* [Understanding Java code](#understanding-java-code)
    * [Java Classes](#java-classes)
        * [Person](#person)
        * [PersonResource](#personcontextinitializers)
    * [Application.properties](#application-properties)    
* [Compiling in Quarkus Cloud Native]()   
    * [Running the application in native mod]() 
* [Related Guides](#Related-guides)    


## Staring PostgreSQL
This session will guide you through obtaining the Red Hat PostgreSQL distribution image and running it using Podman.

### Obtaining PostgreSQL Red Hat image

1. Login to the Red Hat Registry for accessing images:
```shell
$ podman login -u <user> registry.redhat.io
```
```console
Login Succeeded!
```

2. Obtaining PostgreSQL image:
```shell
$ podman pull registry.redhat.io/rhel9/postgresql-15:1-14
```
```console
Trying to pull registry.redhat.io/rhel9/postgresql-15:1-14...
Getting image source signatures
Checking if image destination supports signatures
Copying blob d8a5c3ac3c85 done  
Copying blob 7b3dd25bf011 done  
Copying blob 0b29986fa3e6 done  
Copying config dbca9dc9de done  
Writing manifest to image destination
Storing signatures
dbca9dc9de346510bfdb36dc72f5610e379a6e05bf6323392ef205e179326115
```testing-api-application
```console
Hibernate: 
    drop table if exists Person cascade
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2023-06-26 12:22:44,577 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (JPA Startup Thread) SQL Warning Code: 0, SQLState: 00000

2023-06-26 12:22:44,579 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (JPA Startup Thread) table "person" does not exist, skipping
Hibernate: 
    drop sequence if exists Person_SEQ
2023-06-26 12:22:44,580 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (JPA Startup Thread) SQL Warning Code: 0, SQLState: 00000
2023-06-26 12:22:44,581 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (JPA Startup Thread) sequence "person_seq" does not exist, skipping
Hibernate: 
    create sequence Person_SEQ start with 1 increment by 50
Hibernate: 
    create table Person (
        id bigint not null,
        birth varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        primary key (id)
    )

2023-06-26 12:22:44,675 INFO  [io.quarkus] (Quarkus Main Thread) quarkus-crud-postgres 1.0.0-SNAPSHOT on JVM (powered by Quarkus 3.1.2.Final) started in 2.427s. Listening on: http://localhost:8080
2023-06-26 12:22:44,677 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2023-06-26 12:22:44,677 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [agroal, cdi, hibernate-orm, hibernate-orm-panache, jdbc-postgresql, narayana-jta, resteasy-reactive, resteasy-reactive-jackson, resteasy-reactive-jsonb, smallrye-context-propagation, smallrye-openapi, swagger-ui, vertx]
```

## Testing API application
"For this tutorial, we will use 'curl' to test the application's API. However, if you prefer, you can also access the Swagger UI at http://localhost:8080/q/swagger-ui to interact with the APIs."

image::image.png
![alt text](https://github.com/pedroarraes/quarkus-crud-postgres/blob/main/image.png?raw=true)


1. Performing a new data insertion:

```shell
curl -X 'POST' \
  'http://localhost:8080/person' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": 0,
  "firstName": "Pedro",
  "lastName": "Arraes",
  "birth": "03/17/1983"
}'
```
```sql
Hibernate: 
    insert 
    into
        Person
        (birth,firstName,lastName,id) 
    values
        (?,?,?,?)
```