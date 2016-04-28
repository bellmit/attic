package com.unreasonent.ds.frontend.resources;

import com.unreasonent.ds.frontend.api.FrontendView;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class FrontendTest {
    @Test
    public void getFrontendView() {
        Frontend resource = new Frontend();

        FrontendView view = resource.get();

        assertThat(view, not(nullValue()));
    }
}