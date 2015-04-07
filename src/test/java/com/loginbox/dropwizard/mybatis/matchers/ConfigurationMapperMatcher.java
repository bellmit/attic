package com.loginbox.dropwizard.mybatis.matchers;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ConfigurationMapperMatcher extends TypeSafeDiagnosingMatcher<Configuration> {
    private final Class<?> expectedMapper;

    @Factory
    public static ConfigurationMapperMatcher hasMapper(Class<?> expectedMapper) {
        return new ConfigurationMapperMatcher(expectedMapper);
    }

    public ConfigurationMapperMatcher(Class<?> expectedMapper) {
        this.expectedMapper = expectedMapper;
    }

    @Override
    protected boolean matchesSafely(Configuration item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("was null");
            return false;
        }

        MapperRegistry mapperRegistry = item.getMapperRegistry();
        if (mapperRegistry == null) {
            mismatchDescription.appendText("had no mapper registry");
            return false;
        }

        if (!mapperRegistry.hasMapper(expectedMapper)) {
            mismatchDescription
                    .appendText("had mappers: ")
                    .appendValueList("[", ", ", "]", mapperRegistry.getMappers());
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("has mapper: ")
                .appendValue(expectedMapper);
    }
}
