package com.unreasonent.ds.squad.query;

import com.unreasonent.ds.squad.api.Character;
import com.unreasonent.ds.squad.api.Squad;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SquadResultTest {
    @Test
    public void makesSquad() {
        CharacterResult characterResult = mock(CharacterResult.class);
        Character character = mock(Character.class);
        when(characterResult.toCharacter()).thenReturn(character);

        SquadResult result = new SquadResult("username");
        result.setCharacters(Arrays.asList(characterResult));

        assertThat(result.toSquad(), equalTo(new Squad(Arrays.asList(character))));
    }
}