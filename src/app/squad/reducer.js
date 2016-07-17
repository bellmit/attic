'use strict'

import { combineReducers } from 'redux'
import { handleActions } from 'redux-actions'
import * as actions from './actions'
import * as options from './character-options'
import { randomElement } from 'app/arrays'
import * as sg from './string-generator'

const nameGenerator = sg.load(require('./name-generator.yaml'))

function randomSprite() {
  return {
    hat: randomElement(options.HATS),
    hair: randomElement(options.HAIRS),
    outfit: randomElement(options.OUTFITS),
    gender: randomElement(options.GENDERS),
  }
}

function randomName() {
  return nameGenerator.generate('name')
}

const initialCharacters = [
  {
    name: randomName(),
    archetype: randomElement(options.ARCHETYPES),
    sprite: randomSprite(),
  },
  {
    name: randomName(),
    archetype: randomElement(options.ARCHETYPES),
    sprite: randomSprite(),
  },
  {
    name: randomName(),
    archetype: randomElement(options.ARCHETYPES),
    sprite: randomSprite(),
  },
]

const sprite = handleActions({
  UPDATE_CHARACTER_SPRITE: (state, {payload}) => ({
    ...state,
    ...payload,
  }),
})

const character = handleActions({
  CHANGE_CHARACTER_NAME: (state, action) => ({
    ...state,
    name: action.payload,
  }),
  CHANGE_CHARACTER_ARCHETYPE: (state, action) => ({
    ...state,
    archetype: action.payload,
  }),
  UPDATE_CHARACTER_SPRITE: (state, action) => ({
    ...state,
    sprite: sprite(state.sprite, action),
  }),
})

function reduceIndexedElement(reducer) {
  return (state, action) => state.map(
    (elem, index) => action.meta.index == index ? reducer(elem, action) : elem
  )
}

const characters = handleActions({
  CHANGE_CHARACTER_NAME: reduceIndexedElement(character),
  CHANGE_CHARACTER_ARCHETYPE: reduceIndexedElement(character),
  UPDATE_CHARACTER_SPRITE: reduceIndexedElement(character),
  SQUAD_LOADED: (state, action) => action.payload.characters,
  GENERATE_SQUAD: (state, action) => initialCharacters,
}, [])

const workflow = handleActions({
  LOADING_SQUAD: (state, action) => ({
    ...state,
    loading: true,
  }),
  SQUAD_LOADED: (state, action) => ({
    ...state,
    loading: false,
  }),
  GENERATE_SQUAD: (state, action) => ({
    ...state,
    loading: false,
  }),
  SAVING_SQUAD: (state, action) => ({
    ...state,
    saving: true,
  }),
  SQUAD_SAVED: (state, action) => ({
    ...state,
    saving: false,
  }),
}, {
  saving: false,
})

module.exports = combineReducers({
  characters,
  workflow,
})
