package com.unreasonent.ds.squad.query;

import com.unreasonent.ds.squad.api.Archetype;
import com.unreasonent.ds.squad.api.Character;
import com.unreasonent.ds.squad.api.Gender;
import com.unreasonent.ds.squad.api.Sprite;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CharacterResultTest {
    @Test
    public void createsCharacter() {
        CharacterResult result = new CharacterResult(1, "Bob", Archetype.HUNTER);
        Sprite sprite = new Sprite(Gender.F, "1", "1", "2");
        result.setSprite(sprite);

        assertThat(result.toCharacter(), equalTo(new Character("Bob", Archetype.HUNTER, sprite)));
    }
}