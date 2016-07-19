'use strict'

import React, { PropTypes } from 'react'
import { HATS, HAIRS, OUTFITS, GENDERS } from '../character-options'

import CharacterSprite from './CharacterSprite'
import CycleButtons from './CycleButtons'
import OptionButtons from './OptionButtons'

export default function CharacterEditor({
  index,
  sprite,
  name,
  archetype,
  changeCharacterName,
  changeCharacterArchetype,
  updateCharacterSprite,
}) {
  var genders = {
    F: "♀",
    M: "♂",
  }

  var archetypes = {
    SKIRMISHER: <div>
      <h5>Skirmisher</h5>
      <p>Nimble fighter<br />
      Vulnerable to Hunters</p>
    </div>,
    HUNTER: <div>
      <h5>Hunter</h5>
      <p>Stalwart archer<br />
      Vulnerable to Sages</p>
    </div>,
    SAGE: <div>
      <h5>Sage</h5>
      <p>Crafty mystics<br />
      Vulnerable to Skirmishers</p>
    </div>,
  }

  return <div className="row character-editor">
    <div className="col-md-1">
      <div className="form-group hats-input">
        <CycleButtons
          options={HATS}
          value={sprite.hat}
          onSelect={hat => updateCharacterSprite(index, { hat, })} />
      </div>

      <div className="form-group hairs-input">
        <CycleButtons
          options={HAIRS}
          value={sprite.hair}
          onSelect={hair => updateCharacterSprite(index, { hair, })} />
      </div>

      <div className="form-group outfits-input">
        <CycleButtons
          options={OUTFITS}
          value={sprite.outfit}
          onSelect={outfit => updateCharacterSprite(index, { outfit, })} />
      </div>

      <div className="form-group genders-input">
        <OptionButtons
          buttonClassName="btn-sm"
          options={genders}
          value={sprite.gender}
          onSelect={gender => updateCharacterSprite(index, { gender, })} />
      </div>
    </div>

    <div className="col-md-2">
      <CharacterSprite
          width="100"
          height="160"
          className="center-block character-sprite"
          facing="bottom-right"
          {...sprite} />
    </div>

    <div className="col-md-7">
      <div className="form-group name-input">
        <label>Name
          <input
            type="text"
            className="form-control"
            placeholder="Character name…"
            value={name}
            onChange={event => changeCharacterName(index, event.target.value)} />
        </label>
      </div>

      <div className="form-group archetype-input">
        <OptionButtons
          options={archetypes}
          value={archetype}
          onSelect={archetype => changeCharacterArchetype(index, archetype)} />
      </div>
    </div>
  </div>
}
