package com.loginbox.app.acceptance.framework.context;

import io.github.unacceptable.alias.AliasStore;
import io.github.unacceptable.alias.EmailAddressGenerator;
import io.github.unacceptable.alias.PasswordGenerator;
import io.github.unacceptable.alias.UsernameGenerator;

/**
 * Shared state for the current test run. DSL classes can use their test context to convert aliases (used in test cases)
 * into run-unique actual values, while reusing those values within a test run.
 */
public class TestContext {
    public final AliasStore<String> usernames = new AliasStore<>(UsernameGenerator::defaultGenerate);
    public final AliasStore<String> emailAddresses = new AliasStore<>(EmailAddressGenerator::defaultGenerate);
    public final AliasStore<String> passwords = new AliasStore<>(PasswordGenerator::defaultGenerate);
}
