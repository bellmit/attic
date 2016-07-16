'use strict'

import { createAction } from 'redux-actions'
import { push } from 'react-router-redux'

function indexedMetaCreator(index) {
  return {
    index,
  }
}

export const changeCharacterName = createAction(
  'CHANGE_CHARACTER_NAME',
  (index, name) => name,
  indexedMetaCreator
)

export const changeCharacterArchetype = createAction(
  'CHANGE_CHARACTER_ARCHETYPE',
  (index, archetype) => archetype,
  indexedMetaCreator
)

export const updateCharacterSprite = createAction(
  'UPDATE_CHARACTER_SPRITE',
  (index, sprite) => sprite,
  indexedMetaCreator
)

export const savingSquad = createAction('SAVING_SQUAD')
export const loadingSquad = createAction('LOADING_SQUAD')

export const squadSaved = createAction('SQUAD_SAVED')
export const squadLoaded = createAction('SQUAD_LOADED')
export const generateSquad = createAction('GENERATE_SQUAD')

export function loadSquad(api) {
  return dispatch => {
    dispatch(loadingSquad())
    return api.squad()
      .get()
      .then(squad => dispatch(squadLoaded(squad)))
      .catch(err => err.code === 404 ?
        dispatch(generateSquad()) :
        console.error('squad.load.failed', err))
  }
}

export function saveSquad(api) {
  return (dispatch, getState) => {
    var squad = getState().squad

    dispatch(savingSquad())
    return api.squad()
      .update({
        characters: squad.characters,
      })
      .then(() => dispatch(push('/')))
      .catch(err => {
        console.error('squad.save.failed', err)
      })
      .then(() => dispatch(squadSaved()))
  }
}
