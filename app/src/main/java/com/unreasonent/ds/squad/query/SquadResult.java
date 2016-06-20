package com.unreasonent.ds.squad.query;

import com.unreasonent.ds.squad.api.Character;
import com.unreasonent.ds.squad.api.Squad;

import java.util.List;
import java.util.stream.Collectors;

public class SquadResult {
    private List<CharacterResult> characters;

    public SquadResult(/* used by mybatis */ String userId) {
    }

    void setCharacters(List<CharacterResult> characters) {
        this.characters = characters;
    }

    public Squad toSquad() {
        List<Character> characters = this.characters.stream()
                .map(CharacterResult::toCharacter)
                .collect(Collectors.toList());
        return new Squad(characters);
    }
}
