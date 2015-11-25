package io.github.unacceptable.alias;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class AliasStoreTest {
    @Test
    public void generatesAliases() {
        AliasStore<String> aliases = new AliasStore<>(alias -> String.format("%s-generated", alias));

        assertThat(aliases.resolve("alias"), equalTo("alias-generated"));
    }

    @Test
    public void resolvesNull() {
        AliasStore<String> aliases = new AliasStore<>(this::failIfCalled);

        assertThat(aliases.resolve(null), nullValue());
    }

    @Test
    public void resolvesStoredValue() {
        AliasStore<String> aliases = new AliasStore<>(this::failIfCalled);

        aliases.store("alias", "value");
        assertThat(aliases.resolve("alias"), equalTo("value"));
    }

    @Test
    public void resolvesStoredNull() {
        AliasStore<String> aliases = new AliasStore<>(this::failIfCalled);

        aliases.store("alias", null);
        assertThat(aliases.resolve("alias"), nullValue());
    }
    
    @Test
    public void resolvesNonStrings() {
        AliasStore<UUID> aliases = new AliasStore<>(alias -> UUID.randomUUID());

        final UUID uuid1 = aliases.resolve("alias");
        final UUID uuid2 = aliases.resolve("alias");
        assertThat(uuid2, equalTo(uuid1));
    }

    private String failIfCalled(String alias) {
        throw new RuntimeException("generator should not be called");
    }
}

