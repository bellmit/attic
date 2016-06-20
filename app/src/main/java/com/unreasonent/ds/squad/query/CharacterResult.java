package com.unreasonent.ds.squad.query;

import com.unreasonent.ds.squad.api.Archetype;
import com.unreasonent.ds.squad.api.Character;
import com.unreasonent.ds.squad.api.Sprite;

class CharacterResult {
    private final String name;
    private final Archetype archetype;
    private Sprite sprite;

    public CharacterResult(/* used by SQL mapping */ long sequence, String name, Archetype archetype) {
        this.name = name;
        this.archetype = archetype;
    }

    void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Character toCharacter() {
        return new Character(name, archetype, sprite);
    }
}
