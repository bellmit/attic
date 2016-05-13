'use strict';

import { combineReducers } from 'redux';
import * as actions from './actions';
import * as options from './character-options';
import { randomElement, cyclicIndex } from 'app/arrays';

import { NEXT, PREV } from './components/CycleButtons';

function randomSprite() {
  return {
    hat: randomElement(options.HATS),
    hair: randomElement(options.HAIRS),
    outfit: randomElement(options.OUTFITS),
    gender: randomElement(options.GENDERS),
  };
}

const initialCharacters = [
  {
    name: "Player 1",
    sprite: randomSprite(),
  },
  {
    name: "Player 3",
    sprite: randomSprite(),
  },
  {
    name: "Player 2",
    sprite: randomSprite(),
  },
];

function cycle(values, current, direction) {
  var index = values.indexOf(current);
  switch (direction) {
  case NEXT:
    index = cyclicIndex(index + 1, values);
    break;
  case PREV:
    index = cyclicIndex(index - 1, values);
    break;
  }
  return values[index];
}

function sprite(state, action) {
  switch (action.type) {
  case actions.CHANGE_CHARACTER_GENDER:
    return {
      ...state,
      gender: action.gender,
    };
  case actions.CHANGE_CHARACTER_HAT:
    return {
      ...state,
      hat: cycle(options.HATS, state.hat, action.direction),
    };
  case actions.CHANGE_CHARACTER_HAIR:
    return {
      ...state,
      hair: cycle(options.HAIRS, state.hair, action.direction),
    };
  case actions.CHANGE_CHARACTER_OUTFIT:
    return {
      ...state,
      outfit: cycle(options.OUTFITS, state.outfit, action.direction),
    };
  default:
    return state;
  }
}

function characters(state=initialCharacters, action) {
  switch (action.type) {
  case actions.CHANGE_CHARACTER_NAME:
    return state.map((character, index) => {
      if (index == action.index) {
        return {
          ...character,
          name: action.name,
        };
      }
      return character;
    });
  case actions.CHANGE_CHARACTER_GENDER:
  case actions.CHANGE_CHARACTER_HAT:
  case actions.CHANGE_CHARACTER_HAIR:
  case actions.CHANGE_CHARACTER_OUTFIT:
    return state.map((character, index) => {
      if (index == action.index) {
        return {
          ...character,
          sprite: sprite(character.sprite, action),
        };
      }
      return character;
    });
  default:
    return state;
  }
}

module.exports = combineReducers({
  characters,
});
