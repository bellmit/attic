package com.example.dropwizard.api;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Greeting {
    @Id
    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
