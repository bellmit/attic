'use strict';

import React, { PropTypes } from 'react';
import classNames from 'classnames';
import { randomIndex, cyclicIndex } from 'app/arrays';
import { HATS, HAIRS, OUTFITS, GENDERS } from '../character-options';
import * as actions from '../actions';

import CharacterSprite from './CharacterSprite';
import CycleButtons from './CycleButtons';

const CharacterEditor = React.createClass({
  render() {
    return (
      <div className="row">
        <div className="col-md-1">
          <div className="form-group">
            <CycleButtons onCycle={dir => this.props.changeCharacterHat(this.props.index, dir)} />
          </div>

          <div className="form-group">
            <CycleButtons onCycle={dir => this.props.changeCharacterHair(this.props.index, dir)} />
          </div>

          <div className="form-group">
            <CycleButtons onCycle={dir => this.props.changeCharacterOutfit(this.props.index, dir)} />
          </div>

          <div className="form-group">
            <div className="btn-group" data-toggle="buttons">
              {GENDERS.map(gender => (
                <label key={gender}
                  className={classNames({
                    "btn": true,
                    "btn-sm": true,
                    "btn-default": true,
                    "active": this.props.sprite.gender == gender,
                  })}>
                  <input
                    type="radio"
                    name="gender"
                    value={gender}
                    onChange={event => this.props.changeCharacterGender(this.props.index, gender)}
                    checked={this.props.sprite.gender == gender} /> {gender}
                </label>
              ))}
            </div>
          </div>
        </div>

        <div className="col-md-2">
          <CharacterSprite width="100" height="160" className="center-block" facing="bottom-right" {...this.props.sprite} />
        </div>

        <div className="col-md-7">
          <div className="form-group">
            <label>Name
              <input
                type="text"
                className="form-control"
                placeholder="Character name…"
                value={this.props.name}
                onChange={event => this.props.changeCharacterName(this.props.index, event.target.value)} />
            </label>
          </div>
        </div>
      </div>
    );
  },
});

module.exports = CharacterEditor;
