package com.unreasonent.ds.squad;

import com.unreasonent.ds.squad.models.PlayerOwnedSquad;
import com.unreasonent.ds.squad.models.UpdateSquadHandler;
import com.unreasonent.ds.squad.query.SquadUpdatedHandler;
import com.unreasonent.ds.squad.resources.SquadResource;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerAdapter;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventsourcing.EventSourcedAggregateRoot;
import org.axonframework.eventsourcing.EventSourcingRepository;

public abstract class SquadBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        /* no init, just runtime */
    }

    @Override
    public void run(Environment environment) {
        CommandBus commandBus = getCommandBus();
        EventBus eventBus = getEventBus();

        subscribePlayerOwnedSquad(commandBus, eventBus);

        environment.jersey().register(new SquadResource(getCommandGateway(), getSqlSessionFactory()));
    }

    private void subscribePlayerOwnedSquad(CommandBus commandBus, EventBus eventBus) {
        EventSourcingRepository<PlayerOwnedSquad> repository = newRepository(PlayerOwnedSquad.class);
        UpdateSquadHandler updateSquadHandler = new UpdateSquadHandler(repository);
        AnnotationCommandHandlerAdapter.subscribe(updateSquadHandler, commandBus);

        SquadUpdatedHandler squadUpdatedHandler = new SquadUpdatedHandler(getSqlSessionFactory());
        AnnotationEventListenerAdapter.subscribe(squadUpdatedHandler, eventBus);
    }

    protected abstract <T extends EventSourcedAggregateRoot<?>>
        EventSourcingRepository<T> newRepository(Class<T> aggregateClass);

    protected abstract DefaultCommandGateway getCommandGateway();

    protected abstract CommandBus getCommandBus();

    protected abstract EventBus getEventBus();

    protected abstract SqlSessionFactory getSqlSessionFactory();
}
