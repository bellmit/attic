package io.github.unacceptable.database;

import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DatabaseContextTest {
    @Test
    public void ruleDrivenDatabaseLifecycle() throws Throwable {
        DatabaseContext context = spy(DatabaseContext.class);
        doAnswer(RETURNS_MOCKS).when(context).create();
        doAnswer(RETURNS_MOCKS).when(context).destroy();

        TestRule rules = context.rules();

        Statement statement = mock(Statement.class);
        Description description = mock(Description.class);

        rules.apply(statement, description).evaluate();

        InOrder callOrder = inOrder(context, statement);
        callOrder.verify(context).create();
        callOrder.verify(statement).evaluate();
        callOrder.verify(context).destroy();
    }

    @Test
    public void computesDatabaseNameOnce() {
        DatabaseContext context = new DatabaseContext();

        String name = context.databaseName();
        assertThat(context.databaseName(), equalTo(name));
    }

    @Test
    public void defaultCreateStatement() throws SQLException {
        DatabaseContext context = spy(DatabaseContext.class);
        doAnswer(RETURNS_MOCKS).when(context).runStatement(any(String.class));
        doReturn("test-database").when(context).databaseName();

        context.create();

        verify(context).runStatement("CREATE DATABASE \"test-database\"");
    }

    @Test
    public void defaultDestroyStatement() throws SQLException {
        DatabaseContext context = spy(DatabaseContext.class);
        doAnswer(RETURNS_MOCKS).when(context).runStatement(any(String.class));
        doReturn("test-database").when(context).databaseName();

        context.destroy();

        verify(context).runStatement("DROP DATABASE \"test-database\"");
    }

    @Test
    public void runStatementUsesAdminConnection() throws SQLException {
        DatabaseContext context = spy(DatabaseContext.class);
        Connection connection = mock(Connection.class, RETURNS_DEEP_STUBS);
        doReturn(connection).when(context).openAdminConnection();

        context.runStatement("SQL statement");

        verify(connection).prepareStatement("SQL statement");
        verify(connection.prepareStatement("SQL statement")).executeUpdate();
        verify(connection.prepareStatement("SQL statement")).close();
        verify(connection).close();
    }
}