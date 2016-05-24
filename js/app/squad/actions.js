'use strict';

import { createAction } from 'redux-actions';
import { push } from 'react-router-redux';

function indexedMetaCreator(index) {
  return {
    index,
  };
}

export const changeCharacterName = createAction(
  'CHANGE_CHARACTER_NAME',
  (index, name) => name,
  indexedMetaCreator
);

export const changeCharacterArchetype = createAction(
  'CHANGE_CHARACTER_ARCHETYPE',
  (index, archetype) => archetype,
  indexedMetaCreator
);

export const updateCharacterSprite = createAction(
  'UPDATE_CHARACTER_SPRITE',
  (index, sprite) => sprite,
  indexedMetaCreator
);

export const savingSquad = createAction('SAVING_SQUAD');
export const loadingSquad = createAction('LOADING_SQUAD');

export const squadSaved = createAction('SQUAD_SAVED');
export const squadLoaded = createAction('SQUAD_LOADED');

export function loadSquad() {
  return dispatch =>
    Promise.resolve()
      .then(() => dispatch(loadingSquad()))
      /* actually load */
      .then(() => new Promise((resolve, reject) => setTimeout(resolve, 1000)))
      .then(() => dispatch(squadLoaded()));
}

export function saveSquad() {
  return dispatch =>
    Promise.resolve()
      .then(() => dispatch(savingSquad()))
      /* actually save */
      .then(() => new Promise((resolve, reject) => setTimeout(resolve, 1000)))
      // Dispatch this _before_ completion so that page nav hides the button
      // state changing
      .then(() => dispatch(push('/')))
      .then(() => dispatch(squadSaved()));
}
