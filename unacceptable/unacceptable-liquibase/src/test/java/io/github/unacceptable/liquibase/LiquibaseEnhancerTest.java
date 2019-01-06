package io.github.unacceptable.liquibase;

import io.github.unacceptable.database.DatabaseContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;

import java.sql.Connection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class LiquibaseEnhancerTest {

    @Ignore("mockito is having issues with this for some reason")
    @Test
    public void ruleDrivenDatabaseLifecycle() throws Throwable {
        DatabaseContext context = spy(DatabaseContext.class);
        doAnswer(RETURNS_MOCKS).when(context).create();
        doAnswer(RETURNS_MOCKS).when(context).openConnection();
        doAnswer(RETURNS_MOCKS).when(context).destroy();

        LiquibaseEnhancer enhancer = spy(LiquibaseEnhancer.class);
        doAnswer(RETURNS_MOCKS).when(enhancer).migrate(any(Connection.class));

        enhancer.configure(context);

        TestRule rules = context.rules();

        Statement statement = mock(Statement.class);
        Description description = mock(Description.class);

        rules.apply(statement, description).evaluate();

        InOrder callOrder = inOrder(context, enhancer, statement);
        callOrder.verify(context).create();
        callOrder.verify(enhancer).migrate(context.openConnection());
        callOrder.verify(statement).evaluate();
        callOrder.verify(context).destroy();
    }
}