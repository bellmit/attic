'use strict';

import { createAction } from 'redux-actions';

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
