package com.loginbox.app.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginbox.app.csrf.CsrfToken;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class FormConventionTest {
    public static class SampleForm extends FormConvention {
        @JsonCreator
        public SampleForm(@JsonProperty("csrfToken") CsrfToken token) {
            super(token, "/example.ftl");
        }
    }

    private final CsrfToken csrfToken = mock(CsrfToken.class);

    @Test
    public void viewNames() {
        FormConvention form = new FormConvention(csrfToken, "/example.ftl");
        assertThat(form.getTemplateName(), is("/example.ftl"));
    }

    @Test
    public void includesToken() {
        FormConvention form = new FormConvention(csrfToken, "/example.ftl");
        assertThat(form.getCsrfToken(), is(csrfToken));
    }

    @Test
    public void parsesCsrfToken() throws IOException {
        String json = "{\"csrfToken\": \"abcd\"}";
        CsrfToken expectedToken = new CsrfToken("abcd");

        ObjectMapper mapper = new ObjectMapper();

        SampleForm parsedForm = mapper.readValue(json, SampleForm.class);
        assertThat(parsedForm.getCsrfToken(), is(equalTo(expectedToken)));
    }

    @Test
    public void parsesOmittedCsrfToken() throws IOException {
        String json = "{}";

        ObjectMapper mapper = new ObjectMapper();

        SampleForm parsedForm = mapper.readValue(json, SampleForm.class);
        assertThat(parsedForm.getCsrfToken(), is(nullValue()));
    }

    @Test
    public void serializesCsrfToken() throws IOException {
        CsrfToken csrfToken = new CsrfToken("abcd");
        FormConvention form = new FormConvention(csrfToken, "/example.ftl");
        String expectedJson = "{\"links\":{},\"csrfToken\":\"abcd\"}";

        ObjectMapper mapper = new ObjectMapper();

        String serializedJson = mapper.writeValueAsString(form);

        assertThat(serializedJson, is(equalTo(expectedJson)));
    }

    @Test
    public void serializationOmitsNullCsrfToken() throws IOException {
        FormConvention form = new FormConvention(null, "/example.ftl");
        String expectedJson = "{\"links\":{}}";

        ObjectMapper mapper = new ObjectMapper();

        String serializedJson = mapper.writeValueAsString(form);

        assertThat(serializedJson, is(equalTo(expectedJson)));
    }
}
