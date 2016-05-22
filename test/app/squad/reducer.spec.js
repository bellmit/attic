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
          outfit: "1",
          gender: "F",
        },
      },
    ],
  };

  it('changes character names', () => {
    var toState = reducer(fromState, actions.changeCharacterName(0, "Doug"));

    expect(toState).to.deep.equal({
      characters: [
        {
          name: "Doug",
          sprite: {
            hair: "1",
            hat: "0",
            outfit: "1",
            gender: "F",
          },
        },
      ],
    });
  });

  it('updates character sprites', () => {
    var toState = reducer(fromState, actions.updateCharacterSprite(0, {
      hair: "2",
      hat: "1",
      outfit: "2",
      gender: "M",
    }));

    expect(toState).to.deep.equal({
      characters: [
        {
          name: "Bob",
          sprite: {
            hair: "2",
            hat: "1",
            outfit: "2",
            gender: "M",
          },
        },
      ],
    });
  });

  it('partially updates character sprites', () => {
    var toState = reducer(fromState, actions.updateCharacterSprite(0, {
      hair: "2",
      gender: "M",
    }));

    expect(toState).to.deep.equal({
      characters: [
        {
          name: "Bob",
          sprite: {
            hair: "2",
            hat: "0",
            outfit: "1",
            gender: "M",
          },
        },
      ],
    });
  });

  it('ignores updates to a nonexistant character', () => {
    var toState = reducer(fromState, actions.updateCharacterSprite(1, {
      hair: "2",
      hat: "1",
      outfit: "2",
      gender: "M",
    }));

    expect(toState).to.deep.equal(fromState);
  });
});
