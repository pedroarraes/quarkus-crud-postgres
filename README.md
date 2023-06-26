

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
        * [PersonResource](#person-resource)
    * [Application Properties](#application-properties)    
* [Compiling Application to Quarkus Cloud Native](#compiling-application-to-quarkus-cloud-native)   
    * [Running the application in native mod](#running-the-application-in-native-mod) 
* [Related Guides](#related-guides)    


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

#### Person (#person)

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
#### Person Resource

This class exposes our CRUD methods through a REST API using RESTEasy Reactive. Let's take a look at the key annotations used.

Let's explore some annotations:

* @Path - Mark the class as a REST API and specify the HTTP address to access its methods.
* @POST - This annotation is used to indicate that a method handles HTTP POST requests. It is commonly used for creating or adding new resources to the server. In RESTful APIs, a POST request typically submits data to be processed.
* @GET - This annotation is used to indicate that a method handles HTTP GET requests. It is used for retrieving or reading resources from the server. GET requests are generally used to retrieve data and should not have any side effects on the server.
* @DELETE - This annotation is used to indicate that a method handles HTTP DELETE requests. It is used for deleting or removing resources from the server. When a DELETE request is sent to a specific resource, it should be removed from the server.
* @PUT - This annotation is used to indicate that a method handles HTTP PUT requests. It is typically used for updating or replacing existing resources on the server. A PUT request requires sending the complete representation of the resource that needs to be updated.

```java
package org.acme;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

@Path("/person")
public class PersonResource {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Person person) {

        Person.create(person);
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person getById(String id) {
        Person person = Person.findById(id);

        if(person == null) {
            throw new WebApplicationException("Person with id: %s doesn't exists");
        }

        return person;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> list() {
        
        return Person.listAll();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteById(String id) {

        Person.deletePerson(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updatePerson(Person person) {

        Person.updatePerson(person);
    }
}
```
### Application Properties

* quarkus.swagger-ui.always-include = true - Includes swagger-ui to application;
* quarkus.datasource.db-kind = postgresql - Specifies which database is being used;
* quarkus.datasource.username = admin - Specifies database username;
* quarkus.datasource.password = admin - Specifies database password;
* quarkus.datasource.jdbc.url = jdbc:postgresql://localhost:5432/crud - Specifies database url access path;
* quarkus.hibernate-orm.log.format-sql=true - Shows SQL formated;
* quarkus.hibernate-orm.log.sql=true - Show SQL in application log;
* quarkus.hibernate-orm.database.generation=drop-and-create - Drops and creates the database when the application starts.

## Compiling Application to Quarkus Cloud Native
In this session, we will explore how to compile this application into a native cloud format, enabling direct deployment on OpenShift.

Building an executable file:
```shell
$ mvn clean package -Pnative
```
```console
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< org.acme:quarkus-crud-postgres >-------------------
[INFO] Building quarkus-crud-postgres 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.2.0:clean (default-clean) @ quarkus-crud-postgres ---
[INFO] Deleting /home/parraes/quarkus/quarkus-crud-postgres/target
[INFO] 
[INFO] --- resources:3.3.0:resources (default-resources) @ quarkus-crud-postgres ---
[INFO] Copying 2 resources
[INFO] 
[INFO] --- quarkus:3.1.2.Final:generate-code (default) @ quarkus-crud-postgres ---
[INFO] 
[INFO] --- compiler:3.11.0:compile (default-compile) @ quarkus-crud-postgres ---
[INFO] Changes detected - recompiling the module! :source
[INFO] Compiling 2 source files with javac [debug release 17] to target/classes
[INFO] 
[INFO] --- quarkus:3.1.2.Final:generate-code-tests (default) @ quarkus-crud-postgres ---
[INFO] 
[INFO] --- resources:3.3.0:testResources (default-testResources) @ quarkus-crud-postgres ---
[INFO] skip non existing resourceDirectory /home/parraes/quarkus/quarkus-crud-postgres/src/test/resources
[INFO] 
[INFO] --- compiler:3.11.0:testCompile (default-testCompile) @ quarkus-crud-postgres ---
[INFO] Changes detected - recompiling the module! :dependency
[INFO] 
[INFO] --- surefire:3.0.0:test (default-test) @ quarkus-crud-postgres ---
[INFO] 
[INFO] --- jar:3.3.0:jar (default-jar) @ quarkus-crud-postgres ---
[INFO] Building jar: /home/parraes/quarkus/quarkus-crud-postgres/target/quarkus-crud-postgres-1.0.0-SNAPSHOT.jar
[INFO] 
[INFO] --- quarkus:3.1.2.Final:build (default) @ quarkus-crud-postgres ---
[WARNING] [io.quarkus.deployment.pkg.steps.NativeImageBuildStep] Cannot find the `native-image` in the GRAALVM_HOME, JAVA_HOME and System PATH. Install it using `gu install native-image` Attempting to fall back to container build.
[INFO] [io.quarkus.deployment.pkg.steps.JarResultBuildStep] Building native image source jar: /home/parraes/quarkus/quarkus-crud-postgres/target/quarkus-crud-postgres-1.0.0-SNAPSHOT-native-image-source-jar/quarkus-crud-postgres-1.0.0-SNAPSHOT-runner.jar
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildStep] Building native image from /home/parraes/quarkus/quarkus-crud-postgres/target/quarkus-crud-postgres-1.0.0-SNAPSHOT-native-image-source-jar/quarkus-crud-postgres-1.0.0-SNAPSHOT-runner.jar
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildContainerRunner] Using podman to run the native image builder
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildContainerRunner] Checking image status quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.3-java17
3207ac2d5882cda8295860b107c8590d9aeaf7bc1058c5e84eb06029f2081ab2
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildStep] Running Quarkus native-image plugin on native-image 22.3.2.1-Final Mandrel Distribution (Java Version 17.0.7+7)
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildRunner] podman run --env LANG=C --rm --user 1000:1000 --userns=keep-id -v /home/parraes/quarkus/quarkus-crud-postgres/target/quarkus-crud-postgres-1.0.0-SNAPSHOT-native-image-source-jar:/project:z --name build-native-miZIp quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.3-java17 -J-Djava.util.logging.manager=org.jboss.logmanager.LogManager -J-DCoordinatorEnvironmentBean.transactionStatusManagerEnable=false -J-Dsun.nio.ch.maxUpdateArraySize=100 -J-Dlogging.initial-configurator.min-level=500 -J-Dvertx.logger-delegate-factory-class-name=io.quarkus.vertx.core.runtime.VertxLogDelegateFactory -J-Dvertx.disableDnsResolver=true -J-Dio.netty.leakDetection.level=DISABLED -J-Dio.netty.allocator.maxOrder=3 -J-Duser.language=en -J-Duser.country=US -J-Dfile.encoding=UTF-8 --features=io.quarkus.caffeine.runtime.graal.CacheConstructorsFeature,org.hibernate.graalvm.internal.GraalVMStaticFeature,io.quarkus.runner.Feature,io.quarkus.runtime.graal.DisableLoggingFeature,io.quarkus.jdbc.postgresql.runtime.graal.SQLXMLFeature,io.quarkus.hibernate.orm.runtime.graal.DisableLoggingFeature -J--add-exports=java.security.jgss/sun.security.krb5=ALL-UNNAMED -J--add-opens=java.base/java.text=ALL-UNNAMED -J--add-opens=java.base/java.io=ALL-UNNAMED -J--add-opens=java.base/java.lang.invoke=ALL-UNNAMED -J--add-opens=java.base/java.util=ALL-UNNAMED -H:+CollectImageBuildStatistics -H:ImageBuildStatisticsFile=quarkus-crud-postgres-1.0.0-SNAPSHOT-runner-timing-stats.json -H:BuildOutputJSONFile=quarkus-crud-postgres-1.0.0-SNAPSHOT-runner-build-output-stats.json -H:+AllowFoldMethods -J-Djava.awt.headless=true --no-fallback --link-at-build-time -H:+ReportExceptionStackTraces -H:-AddAllCharsets --enable-url-protocols=http,https -H:-UseServiceLoaderFeature -H:+StackTrace -J--add-exports=org.graalvm.sdk/org.graalvm.nativeimage.impl=ALL-UNNAMED --exclude-config io\.netty\.netty-codec /META-INF/native-image/io\.netty/netty-codec/generated/handlers/reflect-config\.json --exclude-config io\.netty\.netty-handler /META-INF/native-image/io\.netty/netty-handler/generated/handlers/reflect-config\.json quarkus-crud-postgres-1.0.0-SNAPSHOT-runner -jar quarkus-crud-postgres-1.0.0-SNAPSHOT-runner.jar
========================================================================================================================
GraalVM Native Image: Generating 'quarkus-crud-postgres-1.0.0-SNAPSHOT-runner' (executable)...
========================================================================================================================
[1/7] Initializing...                                                                                   (10.5s @ 0.44GB)
 Version info: 'GraalVM 22.3.2.1-Final Java 17 Mandrel Distribution'
 Java version info: '17.0.7+7'
 C compiler: gcc (redhat, x86_64, 8.5.0)
 Garbage collector: Serial GC
 7 user-specific feature(s)
 - io.quarkus.caffeine.runtime.graal.CacheConstructorsFeature
 - io.quarkus.hibernate.orm.runtime.graal.DisableLoggingFeature: Disables INFO logging during the analysis phase
 - io.quarkus.jdbc.postgresql.runtime.graal.SQLXMLFeature
 - io.quarkus.runner.Feature: Auto-generated class by Quarkus from the existing extensions
 - io.quarkus.runtime.graal.DisableLoggingFeature: Disables INFO logging during the analysis phase
 - org.eclipse.angus.activation.nativeimage.AngusActivationFeature
 - org.hibernate.graalvm.internal.GraalVMStaticFeature: Hibernate ORM's static reflection registrations for GraalVM
[2/7] Performing analysis...  [*******]                                                                 (53.1s @ 3.63GB)
  20,840 (91.50%) of 22,777 classes reachable
  27,998 (61.65%) of 45,418 fields reachable
  98,382 (54.36%) of 180,986 methods reachable
     704 classes,   124 fields, and 3,648 methods registered for reflection
      63 classes,    68 fields, and    55 methods registered for JNI access
       4 native libraries: dl, pthread, rt, z
[3/7] Building universe...                                                                               (6.1s @ 3.64GB)
[4/7] Parsing methods...      [**]                                                                       (4.6s @ 1.91GB)
[5/7] Inlining methods...     [****]                                                                     (2.3s @ 4.97GB)
[6/7] Compiling methods...    [*****]                                                                   (31.6s @ 2.72GB)
[7/7] Creating image...                                                                                  (6.8s @ 2.41GB)
  35.48MB (46.14%) for code area:    63,446 compilation units
  41.10MB (53.44%) for image heap:  457,139 objects and 28 resources
 330.28KB ( 0.42%) for other data
  76.91MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 packages in code area:                               Top 10 object types in image heap:
   1.63MB sun.security.ssl                                     8.04MB byte[] for code metadata
   1.06MB java.util                                            5.19MB java.lang.Class
 814.88KB java.lang.invoke                                     4.12MB byte[] for embedded resources
 717.59KB com.sun.crypto.provider                              4.10MB java.lang.String
 502.11KB java.lang                                            3.69MB byte[] for java.lang.String
 483.52KB org.postgresql.jdbc                                  3.32MB byte[] for general heap data
 473.08KB io.quarkus.runtime.generated                         1.75MB com.oracle.svm.core.hub.DynamicHubCompanion
 450.95KB java.util.concurrent                                 1.02MB byte[] for reflection metadata
 450.59KB sun.security.x509                                  813.97KB java.lang.String[]
 446.14KB org.hibernate.query.hql.internal                   743.39KB java.util.HashMap$Node
  28.08MB for 894 more packages                                7.38MB for 4770 more object types
------------------------------------------------------------------------------------------------------------------------
                        5.6s (4.6% of total time) in 47 GCs | Peak RSS: 7.16GB | CPU load: 7.76
------------------------------------------------------------------------------------------------------------------------
Produced artifacts:
 /project/quarkus-crud-postgres-1.0.0-SNAPSHOT-runner (executable)
 /project/quarkus-crud-postgres-1.0.0-SNAPSHOT-runner-build-output-stats.json (json)
 /project/quarkus-crud-postgres-1.0.0-SNAPSHOT-runner-timing-stats.json (raw)
 /project/quarkus-crud-postgres-1.0.0-SNAPSHOT-runner.build_artifacts.txt (txt)
========================================================================================================================
Finished generating 'quarkus-crud-postgres-1.0.0-SNAPSHOT-runner' in 2m 2s.
[INFO] [io.quarkus.deployment.pkg.steps.NativeImageBuildRunner] podman run --env LANG=C --rm --user 1000:1000 --userns=keep-id -v /home/parraes/quarkus/quarkus-crud-postgres/target/quarkus-crud-postgres-1.0.0-SNAPSHOT-native-image-source-jar:/project:z --entrypoint /bin/bash quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.3-java17 -c objcopy --strip-debug quarkus-crud-postgres-1.0.0-SNAPSHOT-runner
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 159696ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:43 min
[INFO] Finished at: 2023-06-26T15:33:23-03:00
[INFO] ------------------------------------------------------------------------
[WARNING] 
[WARNING] Plugin validation issues were detected in 1 plugin(s)
[WARNING] 
[WARNING]  * org.apache.maven.plugins:maven-resources-plugin:3.3.0
[WARNING] 
[WARNING] For more or less details, use 'maven.plugin.validation' property with one of the values (case insensitive): [BRIEF, DEFAULT, VERBOSE]
```

### Running the application in native mod
To run the application using the generated file, execute:
```shell
$ target/./quarkus-crud-postgres-1.0.0-SNAPSHOT-runner
```
```console
Hibernate: 
    drop table if exists Person cascade
Hibernate: 
    drop sequence if exists Person_SEQ
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
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2023-06-26 15:38:53,969 INFO  [io.quarkus] (main) quarkus-crud-postgres 1.0.0-SNAPSHOT native (powered by Quarkus 3.1.2.Final) started in 0.103s. Listening on: http://0.0.0.0:8080
2023-06-26 15:38:53,969 INFO  [io.quarkus] (main) Profile prod activated. 
2023-06-26 15:38:53,969 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, hibernate-orm-panache, jdbc-postgresql, narayana-jta, resteasy-reactive, resteasy-reactive-jackson, resteasy-reactive-jsonb, smallrye-context-propagation, smallrye-openapi, swagger-ui, vertx]
```
## Related Guides
* HIBERNATE ORM WITH PANACHE ([guide](https://quarkus.io/extensions/io.quarkus/quarkus-hibernate-orm-panache))
* WRITING REST SERVICES WITH RESTEASY REACTIVE ([guide](https://quarkus.io/guides/resteasy-reactive))

