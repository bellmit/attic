package com.loginbox.app.directory.internal;

import com.loginbox.app.directory.Directory;

import java.util.UUID;

public class InternalDirectory implements Directory {
    private final UUID id;

    public InternalDirectory(InternalDirectoryConfiguration configuration) {
        this.id = configuration.getId();
    }

    @Override
    public UUID getId() {
        return id;
    }
}
