package com.unreasonent.ds.squad.commands;

import com.unreasonent.ds.squad.api.Squad;

public class UpdateSquadCommand {
    private final String userId;
    private final Squad squad;

    public UpdateSquadCommand(String userId, Squad squad) {
        this.userId = userId;
        this.squad = squad;
    }

    public Squad getSquad() {
        return squad;
    }

    public String getUserId() {
        return userId;
    }
}
