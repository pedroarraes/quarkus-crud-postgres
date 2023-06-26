podman pull registry.redhat.io/rhel9/postgresql-15:1-14

podman run -d --name postgresql_database -e POSTGRESQL_USER=admin -e POSTGRESQL_PASSWORD=admin -e POSTGRESQL_DATABASE=crud -p 5432:5432 registry.redhat.io/rhel9/postgresql-15:1-14

podman exec -it postgresql_database bash

psql

\c crud

select * from person;