'use strict';

import { combineReducers } from 'redux';
import { handleActions } from 'redux-actions';
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

const sprite = handleActions({
  [actions.CHANGE_CHARACTER_GENDER]: (state, action) => ({
    ...state,
    gender: action.gender,
  }),
  [actions.CHANGE_CHARACTER_HAT]: (state, action) => ({
    ...state,
    hat: cycle(options.HATS, state.hat, action.direction),
  }),
  [actions.CHANGE_CHARACTER_HAIR]: (state, action) => ({
    ...state,
    hair: cycle(options.HAIRS, state.hair, action.direction),
  }),
  [actions.CHANGE_CHARACTER_OUTFIT]: (state, action) => ({
    ...state,
    outfit: cycle(options.OUTFITS, state.outfit, action.direction),
  }),
});

function character(state, action) {
  switch (action.type) {
  case actions.CHANGE_CHARACTER_NAME:
    return {
      ...state,
      name: action.name,
    };
  case actions.CHANGE_CHARACTER_GENDER:
  case actions.CHANGE_CHARACTER_HAT:
  case actions.CHANGE_CHARACTER_HAIR:
  case actions.CHANGE_CHARACTER_OUTFIT:
    return {
      ...state,
      sprite: sprite(state.sprite, action),
    };
  default:
    return state;
  }
}

function characters(state=initialCharacters, action) {
  switch (action.type) {
  case actions.CHANGE_CHARACTER_NAME:
  case actions.CHANGE_CHARACTER_GENDER:
  case actions.CHANGE_CHARACTER_HAT:
  case actions.CHANGE_CHARACTER_HAIR:
  case actions.CHANGE_CHARACTER_OUTFIT:
    return state.map((c, index) => index == action.index ? character(c, action) : c);
  default:
    return state;
  }
}

module.exports = combineReducers({
  characters,
});
