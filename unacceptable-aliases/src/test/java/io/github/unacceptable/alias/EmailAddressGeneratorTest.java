package io.github.unacceptable.alias;

import org.junit.Test;

import static io.github.unacceptable.matchers.PatternMatcher.pattern;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmailAddressGeneratorTest {
    @Test
    public void generatesAddressesFromTemplate() {
        String template = "user@example.net";
        String generated = new EmailAddressGenerator().generate(template);

        assertThat(generated, pattern("user-[0-9a-f]+@example[.]net"));
    }

    @Test
    public void addsMissingExampleDotComDomain() {
        String template = "user";
        String generated = new EmailAddressGenerator().generate(template);

        assertThat(generated, pattern("user-[0-9a-f]+@example[.]com"));
    }
}
