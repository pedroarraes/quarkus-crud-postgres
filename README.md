

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
* [Validating Data](#validating-data)
* [Understanding Java code](#understanding-java-code)
    * [Java Classes](#java-classes)
        * [Person](#person)
        * [PersonResource](#personcontextinitializers)
    * [Application.properties](#application-properties)    
* [Compiling in Quarkus Cloud Native](#va)   
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
$ curl -X 'POST' \
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

2. Retrieving all the content from the table:
```shell
$ curl -X 'GET' \
  'http://localhost:8080/person' \
  -H 'accept: application/json'
```

```console
[{"id":1,"birth":"03/17/1983","firstName":"Pedro","lastName":"Arraes"},{"id":2,"birth":"04/25/1991","firstName":"Joe","lastName":"Santos"},{"id":3,"birth":"04/25/11992","firstName":"Marta","lastName":"Campos"}]
```

```sql
Hibernate: 
    select
        p1_0.id,
        p1_0.birth,
        p1_0.firstName,
        p1_0.lastName 
    from
        Person p1_0
```

3. Updating incorrect data
```shell
$ curl -X 'PUT' \
  'http://localhost:8080/person' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": 3,
  "firstName": "Marta",
  "lastName": "Campos",
  "birth": "04/25/1992"
}'
```

```sql
Hibernate: 
    select
        p1_0.id,
        p1_0.birth,
        p1_0.firstName,
        p1_0.lastName 
    from
        Person p1_0 
    where
        p1_0.id = any (?)

Hibernate: 
    update
        Person 
    set
        birth=?,
        firstName=?,
        lastName=? 
    where
        id=?
```

4. Fetching table content by ID:
```shell
$ curl -X 'GET' \
  'http://localhost:8080/person/1' \
  -H 'accept: application/json'
```

```console
{"id":1,"birth":"03/17/1983","firstName":"Pedro","lastName":"Arraes"
```

```sql
Hibernate: 
    select
        p1_0.id,
        p1_0.birth,
        p1_0.firstName,
        p1_0.lastName 
    from
        Person p1_0 
    where
        p1_0.id = any (?)

```

5. Deleting table content by ID:
```shell
$ curl -X 'DELETE' \
  'http://localhost:8080/person/2' \
  -H 'accept: */*'
```
```sql
Hibernate: 
    delete 
    from
        Person 
    where
        id=?
```

## Validating Data
Now let's verify in our database if all the data is correct based on the tests conducted.

1. Accessing the PostgreSQL container:
```bash
$ podman exec -it postgresql_database bash
$ psql
```

```console
psql (15.2)
Type "help" for help.

postgres=# 
```

2. Accessing crud database:
```bash
postgres=# \c crud
```

```console
You are now connected to database "crud" as user "postgres".
crud=# 
```

3. Validation dataset:
```bash
crud=# select * from person;
```

```console
 id |   birth    | firstname | lastname 
----+------------+-----------+----------
  1 | 03/17/1983 | Pedro     | Arraes
  3 | 04/25/1992 | Marta     | Campos
(2 rows)
```

## Understanding Java code
In this session, you will gain an understanding of Java code, annotations, and the communication between Panache and RESTEasy Reactive extensions in Quarkus."

### Java Classes

### Person (#person)

This is our an entity class. The entity class refers to a class that represents a persistent data entity in an object-relational mapping (ORM) framework, typically used in the context of databases. An entity class is designed to map to a database table, where each instance of the class represents a record (or row) in that table.In this case, we extend the PanacheEntity class, which implements the fundamental CRUD methods.

Let's explore some annotations:

* @Entity - Mark the Java class as a data entity in an object-relational database. With these annotations, the table structure can be automatically created.
* @Transactional - Indicates that the Java method will perform a data transaction, including adding, updating, or deleting data.

```java
package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@Entity
public class Person extends PanacheEntity {
   
    private String firstName;
    private String lastName;
    private String birth;

    public Person() {
        
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirth() {
        return this.birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Transactional
    public static void create(Person person) {
        Person newPerson = new Person();
        
        newPerson.setFirstName(person.getFirstName());
        newPerson.setLastName(person.getLastName());
        newPerson.setBirth(person.getBirth());
        
        try {
            newPerson.persist();    
        } catch (Exception e) {
            e.addSuppressed(e);
        }
    }

    @Transactional
    public static void deletePerson(String id) {
        
        Person person = Person.findById(id);

        if(person == null) {
            throw new NotFoundException();
        }

        Person.deleteById(id);
    }

    @Transactional
    public static void updatePerson(Person person) {

        Person entity = Person.findById(person.id);

        if(entity == null) {
            throw new NotFoundException();
        }

        entity.setBirth(person.birth);
        entity.setFirstName(person.firstName);
        entity.setLastName(person.lastName);

        Person.persist(entity);

    }
}
```
