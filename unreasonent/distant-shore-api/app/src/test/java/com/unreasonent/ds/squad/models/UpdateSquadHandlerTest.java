package com.unreasonent.ds.squad.models;

import com.unreasonent.ds.squad.api.Squad;
import com.unreasonent.ds.squad.commands.UpdateSquadCommand;
import com.unreasonent.ds.squad.events.SquadUpdatedEvent;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class UpdateSquadHandlerTest {
    private final FixtureConfiguration<PlayerOwnedSquad> fixture
            = Fixtures.newGivenWhenThenFixture(PlayerOwnedSquad.class);

    @Before
    public void setUp() {
        UpdateSquadHandler handler = new UpdateSquadHandler(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(handler);
    }

    @Test
    public void updateCreatesSquads() {
        Squad squad = mock(Squad.class);
        UpdateSquadCommand command = new UpdateSquadCommand("userid", squad);
        fixture.given()
                .when(command)
                .expectEvents(new SquadUpdatedEvent("userid", squad));
    }

    @Test
    public void updateModifiesSquads() {
        Squad originalSquad = mock(Squad.class);
        Squad newSquad = mock(Squad.class);
        UpdateSquadCommand command = new UpdateSquadCommand("userid", newSquad);
        fixture.given(new SquadUpdatedEvent("userid", originalSquad))
                .when(command)
                .expectEvents(new SquadUpdatedEvent("userid", newSquad));
    }

    @Test
    public void identicalUpdateEmitsNothing() {
        Squad squad = mock(Squad.class);
        UpdateSquadCommand command = new UpdateSquadCommand("userid", squad);
        fixture.given(new SquadUpdatedEvent("userid", squad))
                .when(command)
                .expectEvents();
    }
}