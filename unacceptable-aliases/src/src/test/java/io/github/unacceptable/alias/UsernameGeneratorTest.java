package io.github.unacceptable.alias;

import org.junit.Test;

import static io.github.unacceptable.matches.PatternMatcher.pattern;
import static org.hamcrest.MatcherAssert.assertThat;

public class UsernameGeneratorTest {
    @Test
    public void generatedValueIncludesName() {
        String generated = UsernameGenerator.generate("derek");

        assertThat(generated, pattern("derek-[0-9a-f]{16}"));
    }

    @Test
    public void longAlias() {
        String generated = UsernameGenerator.generate("a_very_long_alias");

        assertThat(generated, pattern("a_very_long_ali-[0-9a-f]{16}"));
    }
}