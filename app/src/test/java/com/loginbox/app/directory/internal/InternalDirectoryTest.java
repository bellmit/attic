package com.loginbox.app.directory.internal;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InternalDirectoryTest {
    @Test
    public void remembersId() {
        UUID id = UUID.randomUUID();
        InternalDirectoryConfiguration configuration = new InternalDirectoryConfiguration(id);

        InternalDirectory directory = new InternalDirectory(configuration);

        assertThat(directory.getId(), is(id));
    }
}