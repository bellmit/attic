package com.loginbox.app.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class ViewConventionTest {
    @Test
    public void viewNames() {
        ViewConvention view = new ViewConvention("/example.ftl");
        assertThat(view.getTemplateName(), is("/example.ftl"));
    }

    @Test
    public void customLinks() {
        ViewConvention.Links links = new ViewConvention.Links();
        ViewConvention view = new ViewConvention("/example.ftl", links);

        assertThat(view.getLinks(), is(links));
    }

    @Test
    public void serializesLinksWhenNonempty() throws JsonProcessingException {
        ViewConvention.Links links = new ViewConvention.Links() {
            @Override
            public URI getBase() {
                return URI.create("base-here");
            }
        };
        ViewConvention view = new ViewConvention("/example.ftl", links);
        String expectedJson = "{\"links\":{\"base\":\"base-here\"}}";

        ObjectMapper mapper = new ObjectMapper();

        assertThat(mapper.writeValueAsString(view), is(equalTo(expectedJson)));
    }

    @Test
    public void serializesNoLinksWhenNull() throws JsonProcessingException {
        ViewConvention view = new ViewConvention("/example.ftl", null);
        String expectedJson = "{}";

        ObjectMapper mapper = new ObjectMapper();

        assertThat(mapper.writeValueAsString(view), is(equalTo(expectedJson)));
    }

    @Test
    public void serializesLinksWhenEmpty() throws JsonProcessingException {
        ViewConvention.Links links = new ViewConvention.Links() {
            @Override
            public URI getBase() {
                return null;
            }
        };
        ViewConvention view = new ViewConvention("/example.ftl", links);
        String expectedJson = "{\"links\":{}}";

        ObjectMapper mapper = new ObjectMapper();

        assertThat(mapper.writeValueAsString(view), is(equalTo(expectedJson)));
    }
}