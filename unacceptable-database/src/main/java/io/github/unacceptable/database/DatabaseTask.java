package io.github.unacceptable.database;

import org.junit.rules.TestRule;

/**
 * Allows injecting database related tasks to perform during test setup/teardown. The canonical usecase for this
 * is running migrations. (eg. Liquibase)
 */
public interface DatabaseTask {
    TestRule rules();
}
