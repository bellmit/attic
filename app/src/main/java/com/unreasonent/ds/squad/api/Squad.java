package com.unreasonent.ds.squad.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unreasonent.ds.auth.User;
import com.unreasonent.ds.squad.commands.UpdateSquadCommand;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class Squad {
    public UpdateSquadCommand updateCommand(User user) {
        return new UpdateSquadCommand(user.getUserId(), this);
    }

    @Valid
    @NotNull
    @Size(min = 3, max = 3)
    private final List<Character> characters;

    public Squad(@JsonProperty("characters") List<Character> characters) {
        this.characters = characters;
    }

    @JsonProperty
    public List<Character> getCharacters() {
        return characters;
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
