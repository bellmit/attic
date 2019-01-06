package com.unreasonent.ds.squad;

import com.unreasonent.ds.BundleTestCase;
import com.unreasonent.ds.squad.commands.UpdateSquadCommand;
import com.unreasonent.ds.squad.models.PlayerOwnedSquad;
import com.unreasonent.ds.squad.resources.SquadResource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerAdapter;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventsourcing.EventSourcedAggregateRoot;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SquadBundleTest extends BundleTestCase {
    @SuppressWarnings("rawtypes")
    private final EventSourcingRepository repository = mock(EventSourcingRepository.class);
    private final DefaultCommandGateway commandGateway = mock(DefaultCommandGateway.class);
    private final CommandBus commandBus = mock(CommandBus.class);
    private final EventBus eventBus = mock(EventBus.class);
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);

    private final SquadBundle bundle = spy(new SquadBundle() {
        @Override
        @SuppressWarnings("unchecked")
        protected <T extends EventSourcedAggregateRoot<?>> EventSourcingRepository<T> newRepository(Class<T> aggregateClass) {
            return repository;
        }

        @Override
        public DefaultCommandGateway getCommandGateway() {
            return commandGateway;
        }

        @Override
        public CommandBus getCommandBus() {
            return commandBus;
        }

        @Override
        protected EventBus getEventBus() {
            return eventBus;
        }

        @Override
        protected SqlSessionFactory getSqlSessionFactory() {
            return sqlSessionFactory;
        }
    });

    @Test
    @SuppressWarnings("unchecked")
    public void registrations() {
        bundle.run(environment);

        verify(commandBus).subscribe(eq(UpdateSquadCommand.class.getName()), isA(AnnotationCommandHandlerAdapter.class));
        verify(eventBus).subscribe(isA(AnnotationEventListenerAdapter.class));
        verify(bundle).newRepository(PlayerOwnedSquad.class);

        verify(jersey).register(isA(SquadResource.class));
    }

}
