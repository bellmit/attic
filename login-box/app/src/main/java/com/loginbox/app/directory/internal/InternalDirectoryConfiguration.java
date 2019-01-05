package com.loginbox.app.directory.internal;

import java.util.UUID;

public class InternalDirectoryConfiguration {
    private final UUID id;

    public InternalDirectoryConfiguration(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
