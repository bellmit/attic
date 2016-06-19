package com.unreasonent.ds.squad.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;

public class Sprite {
    @NotNull
    private final Gender gender;
    @NotNull
    @OneOf({"1", "2", "3"})
    private final String hair;
    @NotNull
    @OneOf({"0", "1", "2"})
    private final String hat;
    @NotNull
    @OneOf({"1", "2", "3", "4", "5", "6"})
    private final String outfit;

    @JsonCreator
    public Sprite(
            @JsonProperty("gender") Gender gender,
            @JsonProperty("hair") String hair,
            @JsonProperty("hat") String hat,
            @JsonProperty("outfit") String outfit) {
        this.gender = gender;
        this.hair = hair;
        this.hat = hat;
        this.outfit = outfit;
    }

    @JsonProperty
    public Gender getGender() {
        return gender;
    }

    @JsonProperty
    public String getHair() {
        return hair;
    }

    @JsonProperty
    public String getHat() {
        return hat;
    }

    @JsonProperty
    public String getOutfit() {
        return outfit;
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
