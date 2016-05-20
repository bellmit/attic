'use strict';

import React, { PropTypes } from 'react';
import classNames from 'classnames';
import { randomIndex, cyclicIndex } from 'app/arrays';
import { HATS, HAIRS, OUTFITS, GENDERS } from '../character-options';
import * as actions from '../actions';

import CharacterSprite from './CharacterSprite';
import CycleButtons from './CycleButtons';

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
        <CycleButtons onCycle={dir => changeCharacterHat(index, dir)} />
      </div>

      <div className="form-group">
        <CycleButtons onCycle={dir => changeCharacterHair(index, dir)} />
      </div>

      <div className="form-group">
        <CycleButtons onCycle={dir => changeCharacterOutfit(index, dir)} />
      </div>

      <div className="form-group">
        <div className="btn-group" data-toggle="buttons">
          {GENDERS.map(gender => (
            <label key={gender}
              className={classNames({
                "btn": true,
                "btn-sm": true,
                "btn-default": true,
                "active": sprite.gender == gender,
              })}>
              <input
                type="radio"
                name="gender"
                value={gender}
                onChange={event => changeCharacterGender(index, gender)}
                checked={sprite.gender == gender} /> {gender}
            </label>
          ))}
        </div>
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
