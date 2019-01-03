package com.example.dropwizard.resources;

import com.example.dropwizard.api.Greeting;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/")
public class HelloResource {
    private final SessionFactory sessionFactory;

    public HelloResource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GET
    @Produces("application/json")
    public List<Greeting> greetings() {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("select g from Greeting g");
            return query.list();
        } finally {
            session.close();
        }
    }
}
