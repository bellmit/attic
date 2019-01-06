package com.unreasonent.ds.squad.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unreasonent.ds.squad.api.Squad;

public class SquadUpdatedEvent {
    private final String userId;
    private final Squad squad;

    @JsonCreator
    public SquadUpdatedEvent(
            @JsonProperty("userId") String userId,
            @JsonProperty("squad") Squad squad) {
        this.userId = userId;
        this.squad = squad;
    }

    public String getUserId() {
        return userId;
    }

    public Squad getSquad() {
        return squad;
    }
}
