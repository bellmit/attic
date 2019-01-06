package com.unreasonent.ds.axon;

import com.unreasonent.ds.BundleTestCase;
import io.dropwizard.lifecycle.setup.ExecutorServiceBuilder;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AxonBundleTest extends BundleTestCase {
    private final DataSource dataSource = mock(DataSource.class);
    private final ExecutorServiceBuilder executorServiceBuilder = mock(ExecutorServiceBuilder.class);
    private ExecutorService executorService = mock(ExecutorService.class);

    private final AxonBundle bundle = new AxonBundle() {
        @Override
        protected DataSource getDataSource() {
            return dataSource;
        }
    };

    @Before
    public void configureFakeThreadPool() {
        when(lifecycle.executorService(any()))
                .thenReturn(executorServiceBuilder);
        when(executorServiceBuilder.build()).thenReturn(executorService);
    }

    @Test
    public void providesCommandGateway() {
        bundle.run(environment);

        assertThat(bundle.getCommandGateway(), not(nullValue()));
    }

    @Test
    public void providesEventBus() {
        bundle.run(environment);

        assertThat(bundle.getEventBus(), not(nullValue()));
    }

    @Test
    public void makesRepositories() {
        bundle.run(environment);

        assertThat(bundle.newRepository(TestAggregate.class), not(nullValue()));
    }

    public static class TestAggregate extends AbstractAnnotatedAggregateRoot<String> {
        private static final long serialVersionUID = 0;
        @AggregateIdentifier
        private String id;
    }
}
