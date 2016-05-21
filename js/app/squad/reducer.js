'use strict';

import { combineReducers } from 'redux';
import { handleActions } from 'redux-actions';
import * as actions from './actions';
import * as options from './character-options';
import { randomElement } from 'app/arrays';

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

const sprite = handleActions({
  UPDATE_CHARACTER_SPRITE: (state, {payload}) => ({
    ...state,
    ...payload.sprite,
  }),
});

const character = handleActions({
  CHANGE_CHARACTER_NAME: (state, action) => ({
    ...state,
    name: action.payload.name,
  }),
  UPDATE_CHARACTER_SPRITE: (state, action) => ({
    ...state,
    sprite: sprite(state.sprite, action),
  }),
})

function reduceIndexedElement(reducer) {
  return (state, action) => state.map(
    (elem, index) => action.payload.index == index ? reducer(elem, action) : elem
  );
}

const characters = handleActions({
  CHANGE_CHARACTER_NAME: reduceIndexedElement(character),
  UPDATE_CHARACTER_SPRITE: reduceIndexedElement(character),
}, initialCharacters);

module.exports = combineReducers({
  characters,
});
