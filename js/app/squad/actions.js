'use strict';

export const CHANGE_CHARACTER_NAME = 'squad.CHANGE_CHARACTER_NAME';
export const CHANGE_CHARACTER_GENDER = 'squad.CHANGE_CHARACTER_GENDER';
export const CHANGE_CHARACTER_HAT = 'squad.CHANGE_CHARACTER_HAT';
export const CHANGE_CHARACTER_HAIR = 'squad.CHANGE_CHARACTER_HAIR';
export const CHANGE_CHARACTER_OUTFIT = 'squad.CHANGE_CHARACTER_OUTFIT';

export function changeCharacterName(index, name) {
  return {
    type: CHANGE_CHARACTER_NAME,
    index,
    name,
  };
}

export function changeCharacterGender(index, gender) {
  return {
    type: CHANGE_CHARACTER_GENDER,
    index,
    gender,
  };
}

export function changeCharacterHat(index, direction) {
  return {
    type: CHANGE_CHARACTER_HAT,
    index,
    direction,
  };
}

export function changeCharacterHair(index, direction) {
  return {
    type: CHANGE_CHARACTER_HAIR,
    index,
    direction,
  };
}

export function changeCharacterOutfit(index, direction) {
  return {
    type: CHANGE_CHARACTER_OUTFIT,
    index,
    direction,
  };
}
