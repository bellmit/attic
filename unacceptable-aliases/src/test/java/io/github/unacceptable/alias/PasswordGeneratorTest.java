package io.github.unacceptable.alias;

import org.junit.Test;

import static io.github.unacceptable.matchers.PatternMatcher.pattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class PasswordGeneratorTest {
    @Test
    public void defaultLength() {
        String password = new PasswordGenerator().generate("alias");
        assertThat(password.length(), greaterThan(0));
    }

    @Test
    public void customLength() {
        String password = new PasswordGenerator(4).generate("alias");
        assertThat(password.length(), equalTo(4));
    }

    @Test
    public void customAlphabet() {
        String password = new PasswordGenerator("a").generate("alias");

        assertThat(password, pattern("a+"));
    }

    @Test
    public void customLengthAndAlphabet() {
        String password = new PasswordGenerator(5, "ab").generate("alias");

        assertThat(password, pattern("[ab]{5}"));
    }
}