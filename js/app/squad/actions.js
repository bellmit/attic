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

export function changeCharacterHat(index, hat) {
  return {
    type: CHANGE_CHARACTER_HAT,
    index,
    hat,
  };
}

export function changeCharacterHair(index, hair) {
  return {
    type: CHANGE_CHARACTER_HAIR,
    index,
    hair,
  };
}

export function changeCharacterOutfit(index, outfit) {
  return {
    type: CHANGE_CHARACTER_OUTFIT,
    index,
    outfit,
  };
}
