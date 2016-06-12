package com.unreasonent.ds.axon;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventsourcing.EventSourcedAggregateRoot;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jdbc.DefaultEventEntryStore;
import org.axonframework.eventstore.jdbc.EventEntryStore;
import org.axonframework.eventstore.jdbc.JdbcEventStore;
import org.axonframework.eventstore.jdbc.PostgresEventSqlSchema;
import org.axonframework.serializer.json.JacksonSerializer;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;

public abstract class AxonBundle implements Bundle {
    private AsynchronousCommandBus commandBus = null;
    private DefaultCommandGateway commandGateway = null;
    private JdbcEventStore eventStore = null;
    private SimpleEventBus eventBus = null;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {
        ExecutorService commandBusExecutor = environment.lifecycle()
                .executorService("axon-command-bus-%d")
                .build();
        commandBus = new AsynchronousCommandBus(commandBusExecutor);
        commandGateway = new DefaultCommandGateway(commandBus);
        eventStore = createEventStore(environment);
        eventBus = new SimpleEventBus();
    }

    private JdbcEventStore createEventStore(Environment environment) {
        DataSource dataSource = getDataSource();
        PostgresEventSqlSchema<String> eventSqlSchema = new PostgresEventSqlSchema<>(String.class);
        DefaultEventEntryStore<String> eventEntryStore = new DefaultEventEntryStore<String>(dataSource, eventSqlSchema);
        JacksonSerializer serializer = new JacksonSerializer(environment.getObjectMapper());
        return new JdbcEventStore(eventEntryStore, serializer);
    }

    protected abstract DataSource getDataSource();

    public CommandBus getCommandBus() {
        return commandBus;
    }

    public DefaultCommandGateway getCommandGateway() {
        return commandGateway;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public <T extends EventSourcedAggregateRoot<?>> EventSourcingRepository<T> newRepository(Class<T> aggregateClass) {
        EventSourcingRepository<T> repository = new EventSourcingRepository<>(aggregateClass, eventStore);
        repository.setEventBus(eventBus);

        return repository;
    }
}
