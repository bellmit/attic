package com.unreasonent.ds.squad.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Character {
    @NotNull
    @Size(min = 1)
    private final String name;
    @NotNull
    private final Archetype archetype;
    @Valid
    @NotNull
    private final Sprite sprite;

    @JsonCreator
    public Character(
            @JsonProperty("name") String name,
            @JsonProperty("archetype") Archetype archetype,
            @JsonProperty("sprite") Sprite sprite) {
        this.name = name;
        this.archetype = archetype;
        this.sprite = sprite;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public Archetype getArchetype() {
        return archetype;
    }

    @JsonProperty
    public Sprite getSprite() {
        return sprite;
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
