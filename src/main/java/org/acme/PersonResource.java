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
