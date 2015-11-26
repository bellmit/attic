package com.loginbox.app.identity;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String contactEmail;
    private final String password;

    public User(UUID id, String username, String contactEmail, String password) {
        this.id = id;
        this.username = username;
        this.contactEmail = contactEmail;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }
}
