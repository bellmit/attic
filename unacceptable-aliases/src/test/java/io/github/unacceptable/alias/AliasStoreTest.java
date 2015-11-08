package io.github.unacceptable.alias;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class AliasStoreTest {
    @Test
    public void generatesAliases() {
        AliasStore aliases = new AliasStore(alias -> String.format("%s-generated", alias));

        assertThat(aliases.resolve("alias"), equalTo("alias-generated"));
    }

    @Test
    public void resolvesNull() {
        AliasStore aliases = new AliasStore(this::failIfCalled);

        assertThat(aliases.resolve(null), nullValue());
    }

    @Test
    public void resolvesAbsent() {
        AliasStore aliases = new AliasStore(this::failIfCalled);

        assertThat(aliases.resolve("ABSENT"), nullValue());
    }

    @Test
    public void resolvesStoredValue() {
        AliasStore aliases = new AliasStore(this::failIfCalled);

        aliases.store("alias", "value");
        assertThat(aliases.resolve("alias"), equalTo("value"));
    }

    @Test
    public void resolvesStoredNull() {
        AliasStore aliases = new AliasStore(this::failIfCalled);

        aliases.store("alias", null);
        assertThat(aliases.resolve("alias"), nullValue());
    }

    @Test
    public void generatesFixedValues() {
        AliasStore aliases = new AliasStore(this::failIfCalled);

        assertThat(aliases.resolve("<value>"), equalTo("value"));
    }

    @Test
    public void generatesEmptyValue() {
        AliasStore aliases = new AliasStore(this::failIfCalled);

        assertThat(aliases.resolve("<>"), equalTo(""));
    }

    private String failIfCalled(String alias) {
        throw new RuntimeException("generator should not be called");
    }
}

