package com.loginbox.app.acceptance.framework.driver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.dropwizard.driver.SeleniumDriver;
import io.github.unacceptable.selenium.context.SeleniumContext;
import org.openqa.selenium.By;

import java.io.IOException;

public class ErrorPageDriver extends SeleniumDriver {
    private final ObjectMapper json = new ObjectMapper();

    public static class ErrorPageBody {
        public final Integer code;
        public final String message;

        @JsonCreator
        public ErrorPageBody(
                @JsonProperty("code") Integer code,
                @JsonProperty("message") String message) {
            this.code = code;
            this.message = message;
        }
    }

    public ErrorPageDriver(SeleniumContext selenium, ApplicationContext<?> app) {
        super(selenium, app);
    }

    public Integer getCode() throws IOException {
        return parseBody()
                .code;
    }

    private ErrorPageBody parseBody() throws IOException {
        String errorBody = findElement(By.tagName("pre"))
                .getText();
        return json.readValue(errorBody, ErrorPageBody.class);
    }

    public String getMessage() throws IOException {
        return parseBody()
                .message;
    }
}
