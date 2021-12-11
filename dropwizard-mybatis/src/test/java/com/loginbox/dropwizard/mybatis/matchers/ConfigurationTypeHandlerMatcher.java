package com.loginbox.dropwizard.mybatis.matchers;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ConfigurationTypeHandlerMatcher extends TypeSafeDiagnosingMatcher<Configuration> {
    private final Class<?> expectedMappedType;

    @Factory
    public static ConfigurationTypeHandlerMatcher hasTypeHandler(Class<?> expectedMappedType) {
        return new ConfigurationTypeHandlerMatcher(expectedMappedType);
    }

    public ConfigurationTypeHandlerMatcher(Class<?> expectedMappedType) {
        this.expectedMappedType = expectedMappedType;
    }

    @Override
    protected boolean matchesSafely(Configuration item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("was null");
            return false;
        }

        TypeHandlerRegistry typeHandlerRegistry = item.getTypeHandlerRegistry();
        if (typeHandlerRegistry == null) {
            mismatchDescription.appendText("had no type mapper registry");
            return false;
        }

        if (!typeHandlerRegistry.hasTypeHandler(expectedMappedType)) {
            mismatchDescription
                    .appendText("had type handlers for: ")
                    .appendValueList("[", ", ", "]", typeHandlerRegistry.getTypeHandlers());
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("has type handler for: ")
                .appendValue(expectedMappedType);
    }
}
