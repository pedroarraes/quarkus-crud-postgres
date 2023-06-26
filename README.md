podman exec -it postgresql_database bash

psql

\c crud

select * from person;

# How to create a CRUD application using Quarkus Native + PostgreSQL Database

This smart start demostrates how to use a PostgreSQL Database to create a quarkus native cloud application. This project uses Quarkus, the Supersonic Subatomic Java Framework. If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

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
* [Running the application in dev mode](#running-the-application-in-dev-mode)
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
```
## Running container
To run a container in the background, use the following command:
```shell
$ podman run -d --name postgresql_database -e POSTGRESQL_USER=admin -e POSTGRESQL_PASSWORD=admin -e POSTGRESQL_DATABASE=crud -p 5432:5432 registry.redhat.io/rhel9/postgresql-15:1-14
```
```console
a3b5268012e174c0bf2853e0e4ae8edbbfe01cdab23b6598a595ca1989547d57
```
