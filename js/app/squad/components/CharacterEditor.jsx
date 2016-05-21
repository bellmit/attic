'use strict';

import React, { PropTypes } from 'react';
import { HATS, HAIRS, OUTFITS, GENDERS } from '../character-options';

import CharacterSprite from './CharacterSprite';
import CycleButtons from './CycleButtons';
import OptionButtons from './OptionButtons';

export default function CharacterEditor({
  index,
  sprite,
  name,
  changeCharacterHat,
  changeCharacterHair,
  changeCharacterOutfit,
  changeCharacterGender,
  changeCharacterName,
}) {
  return <div className="row">
    <div className="col-md-1">
      <div className="form-group">
        <CycleButtons
          options={HATS}
          value={sprite.hat}
          onSelect={hat => changeCharacterHat(index, hat)} />
      </div>

      <div className="form-group">
        <CycleButtons
          options={HAIRS}
          value={sprite.hair}
          onSelect={hair => changeCharacterHair(index, hair)} />
      </div>

      <div className="form-group">
        <CycleButtons
          options={OUTFITS}
          value={sprite.outfit}
          onSelect={outfit => changeCharacterOutfit(index, outfit)} />
      </div>

      <div className="form-group">
        <OptionButtons
          options={GENDERS}
          value={sprite.gender}
          onSelect={gender => changeCharacterGender(index, gender)} />
      </div>
    </div>

    <div className="col-md-2">
      <CharacterSprite width="100" height="160" className="center-block" facing="bottom-right" {...sprite} />
    </div>

    <div className="col-md-7">
      <div className="form-group">
        <label>Name
          <input
            type="text"
            className="form-control"
            placeholder="Character name…"
            value={name}
            onChange={event => changeCharacterName(index, event.target.value)} />
        </label>
      </div>
    </div>
  </div>;
}
