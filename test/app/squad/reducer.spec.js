'use strict';

import reducer from 'app/squad/reducer';
import * as actions from 'app/squad/actions';
import * as options from 'app/squad/character-options';
import { NEXT, PREV } from 'app/squad/components/CycleButtons';

describe('app/squad/reducer', () => {
  it('generates an initial state', () => {
    var state = reducer(undefined, '//test-action');
    expect(state).to.have.property('characters').with.lengthOf(3);
    state.characters.map(character => {
      expect(character).to.have.property('name')
        .with.length.at.least(1);

      expect(character).to.have.property('sprite')
      expect(character.sprite).to.have.property('gender')
        .that.is.oneOf(options.GENDERS);
      expect(character.sprite).to.have.property('hair')
        .that.is.oneOf(options.HAIRS);
      expect(character.sprite).to.have.property('hat')
        .that.is.oneOf(options.HATS);
      expect(character.sprite).to.have.property('outfit')
        .that.is.oneOf(options.OUTFITS);
    });
  });

  var fromState = {
    characters: [
      {
        name: "Bob",
        sprite: {
          hair: "1",
          hat: "0",
          gender: "F",
          outfit: "1",
        },
      },
    ],
  };

  it('changes character names', () => {
    var toState = reducer(fromState, actions.changeCharacterName(0, "Doug"));

    expect(toState.characters[0].name).to.equal("Doug");
  });

  it('changes character sprite hairs', () => {
    var toState = reducer(fromState, actions.changeCharacterHair(0, "2"));

    expect(toState.characters[0].sprite.hair).to.equal("2");
  });

  it('changes character sprite hats', () => {
    var toState = reducer(fromState, actions.changeCharacterHat(0, "1"));

    expect(toState.characters[0].sprite.hat).to.equal("1");
  });

  it('changes character sprite genders', () => {
    var toState = reducer(fromState, actions.changeCharacterGender(0, "M"));

    expect(toState.characters[0].sprite.gender).to.equal("M");
  });

  it('changes character sprite outfits', () => {
    var toState = reducer(fromState, actions.changeCharacterOutfit(0, "2"));

    expect(toState.characters[0].sprite.outfit).to.equal("2");
  });
});
