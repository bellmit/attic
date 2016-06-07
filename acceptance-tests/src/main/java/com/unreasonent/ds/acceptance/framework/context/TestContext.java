package com.unreasonent.ds.acceptance.framework.context;

import io.github.unacceptable.alias.AliasStore;
import io.github.unacceptable.alias.UsernameGenerator;

/**
 * State container for a single test execution. Preserves and resolves aliases to values, so that tests do not need to
 * worry about generating unique values for fields such as usernames.
 */
public class TestContext {
    public final AliasStore<String> usernames = new AliasStore<>(UsernameGenerator::defaultGenerate);
    public final AliasStore<String> tokens = new AliasStore<>(new JwtTokenGenerator());
}
