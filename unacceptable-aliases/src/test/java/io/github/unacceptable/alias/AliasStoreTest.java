package io.github.unacceptable.alias;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AliasStoreTest {
    @Test
    public void generatesAliases() {
        AliasStore aliases = new AliasStore(alias -> String.format("%s-generated", alias));

        assertThat(aliases.resolve("alias"), equalTo("alias-generated"));
    }

    @Test
    public void generatesFixedValues() {
        AliasStore aliases = new AliasStore(alias -> {
            throw new RuntimeException("nope");
        });

        assertThat(aliases.resolve("<value>"), equalTo("value"));
    }

    @Test
    public void generatesEmptyValue() {
        AliasStore aliases = new AliasStore(alias -> {
            throw new RuntimeException("nope");
        });

        assertThat(aliases.resolve("<>"), equalTo(""));
    }
}
