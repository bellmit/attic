'use strict';

import { createAction } from 'redux-actions';

export const changeCharacterName = createAction(
  'CHANGE_CHARACTER_NAME',
  (index, name) => ({
    index,
    name,
  })
);

export const updateCharacterSprite = createAction(
  'UPDATE_CHARACTER_SPRITE',
  (index, sprite) => ({
    index,
    sprite,
  })
);
