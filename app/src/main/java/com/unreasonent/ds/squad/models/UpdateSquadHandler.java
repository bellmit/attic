package com.unreasonent.ds.squad.models;

import com.unreasonent.ds.squad.commands.UpdateSquadCommand;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

public class UpdateSquadHandler {
    private final Repository<PlayerOwnedSquad> squads;

    public UpdateSquadHandler(Repository<PlayerOwnedSquad> squads) {
        this.squads = squads;
    }

    @CommandHandler
    public void updateSquad(UpdateSquadCommand command) {
        try {
            PlayerOwnedSquad squad = squads.load(command.getUserId());
            squad.updateSquad(command);
        } catch (AggregateNotFoundException anfe) {
            PlayerOwnedSquad squad = PlayerOwnedSquad.createSquad(command);
            squads.add(squad);
        }
    }
}
