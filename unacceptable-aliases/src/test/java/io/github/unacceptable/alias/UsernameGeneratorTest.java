package io.github.unacceptable.alias;

import org.junit.Test;

import static io.github.unacceptable.matchers.PatternMatcher.pattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class UsernameGeneratorTest {
    @Test
    public void generatedValueIncludesName() {
        String generated = new UsernameGenerator().generate("derek");

        assertThat(generated, pattern("derek-[0-9a-f]{16}"));
    }

    @Test
    public void longAlias() {
        String generated = new UsernameGenerator().generate("a_very_long_alias");

        assertThat(generated, pattern("a_very_long_ali-[0-9a-f]{16}"));
    }

    @Test
    public void customLength() {
        UsernameGenerator generator = new UsernameGenerator(21);
        String generated = generator.generate("alias");

        assertThat(generated, pattern("alia-[0-9a-f]{16}"));
    }

    @Test
    public void customSeparator() {
        UsernameGenerator generator = new UsernameGenerator(21, "/");
        String generated = generator.generate("alias");

        assertThat(generated, pattern("alia/[0-9a-f]{16}"));
    }

    @Test
    public void literal() {
        String password = new UsernameGenerator().generate("<alias>");
        assertThat(password, equalTo("alias"));
    }

    @Test
    public void empty() {
        String password = new UsernameGenerator().generate("");
        assertThat(password, equalTo(""));
    }

    @Test
    public void absent() {
        String password = new UsernameGenerator().generate("ABSENT");
        assertThat(password, nullValue());
    }

}