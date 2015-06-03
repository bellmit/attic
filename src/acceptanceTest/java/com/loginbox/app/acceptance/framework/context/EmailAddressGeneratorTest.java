package com.loginbox.app.acceptance.framework.context;

import org.junit.Test;

import static com.loginbox.app.acceptance.framework.matchers.PatternMatcher.pattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EmailAddressGeneratorTest {
    @Test
    public void generatesAddressesFromTemplate() {
        String template = "user@example.net";
        String generated = EmailAddressGenerator.generate(template);

        assertThat(generated, is(pattern("user-[0-9a-f]+@example[.]net")));
    }

    @Test
    public void addsMissingExampleDotComDomain() {
        String template = "user";
        String generated = EmailAddressGenerator.generate(template);

        assertThat(generated, is(pattern("user-[0-9a-f]+@example[.]com")));
    }
}
