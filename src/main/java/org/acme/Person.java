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