package io.github.unacceptable.alias;

import org.junit.Test;

import static io.github.unacceptable.matchers.PatternMatcher.pattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

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

    @Test
    public void literal() {
        String password = new EmailAddressGenerator().generate("<alias>");
        assertThat(password, equalTo("alias"));
    }

    @Test
    public void empty() {
        String password = new EmailAddressGenerator().generate("");
        assertThat(password, equalTo(""));
    }

    @Test
    public void absent() {
        String password = new EmailAddressGenerator().generate("ABSENT");
        assertThat(password, nullValue());
    }

}
