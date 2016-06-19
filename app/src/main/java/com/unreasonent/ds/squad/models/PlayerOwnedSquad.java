package com.unreasonent.ds.squad.models;

import com.unreasonent.ds.squad.api.Squad;
import com.unreasonent.ds.squad.commands.UpdateSquadCommand;
import com.unreasonent.ds.squad.events.SquadUpdatedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

public class PlayerOwnedSquad extends AbstractAnnotatedAggregateRoot<String> {
    private static final long serialVersionUID = 0;

    @AggregateIdentifier
    private String userId;
    private Squad squad;

    public void updateSquad(UpdateSquadCommand command) {
        apply(new SquadUpdatedEvent(command.getUserId(), command.getSquad()));
    }

    @EventHandler
    public void squadUpdated(SquadUpdatedEvent event) {
        this.userId = event.getUserId();
        this.squad = event.getSquad();
    }

    public static PlayerOwnedSquad createSquad(UpdateSquadCommand command) {
        PlayerOwnedSquad squad = new PlayerOwnedSquad();
        squad.updateSquad(command);

        return squad;
    }
}
