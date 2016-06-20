package com.unreasonent.ds.acceptance.framework.dsl;

import com.lmax.simpledsl.DslParam;
import com.lmax.simpledsl.DslParams;
import com.lmax.simpledsl.OptionalParam;
import com.unreasonent.ds.acceptance.framework.context.TestContext;
import com.unreasonent.ds.acceptance.framework.driver.SquadDriver;
import com.unreasonent.ds.acceptance.framework.driver.SystemDriver;
import com.unreasonent.ds.squad.api.Archetype;
import com.unreasonent.ds.squad.api.Character;
import com.unreasonent.ds.squad.api.Gender;
import com.unreasonent.ds.squad.api.Sprite;
import io.github.unacceptable.dsl.SimpleDsl;

import java.util.ArrayList;
import java.util.List;

public class Squad extends SimpleDsl<SquadDriver, TestContext> {
    public Squad(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::squadDriver, testContext);
    }

    public void ensureNotAuthorized() {
        driver().assertNotAuthorized();
    }

    public void ensureNotFound() {
        driver().assertNotFound();
    }

    public void store(String... args) {
        DslParams params = new DslParams(args,
                squadParams()
        );

        com.unreasonent.ds.squad.api.Squad squad = squadFromParams(params);

        driver().store(squad);
    }

    public void ensureSquad(String... args) {
        DslParams params = new DslParams(args,
                squadParams()
        );

        com.unreasonent.ds.squad.api.Squad squad = squadFromParams(params);

        driver().assertStored(squad);
    }

    private static DslParam[] squadParams() {
        return new DslParam[]{new OptionalParam("name")
                .setAllowMultipleValues(),
                new OptionalParam("archetype")
                        .setAllowMultipleValues(),
                new OptionalParam("gender")
                        .setAllowMultipleValues(),
                new OptionalParam("hat")
                        .setAllowMultipleValues(),
                new OptionalParam("hair")
                        .setAllowMultipleValues(),
                new OptionalParam("outfit")
                        .setAllowMultipleValues()};
    }

    private String[] defaultValues(String[] values, String... defaults) {
        if (values.length > 0)
            return values;
        return defaults;
    }

    private com.unreasonent.ds.squad.api.Squad squadFromParams(DslParams params) {
        List<Character> characters = new ArrayList<>();

        String[] names = defaultValues(params.values("name"), "character-1", "character-2", "character-3");
        String[] archetypes = defaultValues(params.values("archetype"), "SKIRMISHER", "HUNTER", "SAGE");
        assert (archetypes.length == names.length);
        String[] genders = defaultValues(params.values("gender"), "F", "F", "M");
        assert (genders.length == names.length);
        String[] hats = defaultValues(params.values("hat"), "1", "1", "1");
        assert (hats.length == names.length);
        String[] hairs = defaultValues(params.values("hair"), "1", "1", "1");
        assert (hairs.length == names.length);
        String[] outfits = defaultValues(params.values("outfit"), "1", "1", "1");
        assert (outfits.length == names.length);

        for (int i = 0; i < names.length; ++i) {
            Gender gender
                    = Gender.valueOf(genders[i]);
            Sprite sprite
                    = new Sprite(gender, hairs[i], hats[i], outfits[i]);
            Archetype archetype
                    = Archetype.valueOf(archetypes[i]);
            Character character
                    = new Character(names[i], archetype, sprite);
            characters.add(character);
        }

        return new com.unreasonent.ds.squad.api.Squad(characters);
    }
}
