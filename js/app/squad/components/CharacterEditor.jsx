'use strict';

import React, { PropTypes } from 'react';
import classNames from 'classnames';
import { randomIndex, cyclicIndex } from 'app/arrays';
import { HATS, HAIRS, OUTFITS, GENDERS } from '../character-options';
import * as actions from '../actions';

import CharacterSprite from './CharacterSprite';
import CycleButtons from './CycleButtons';

const dataProps = {
  name: PropTypes.string.isRequired,
  sprite: CharacterSprite.dataProps.isRequired,
};

const CharacterEditor = React.createClass({
  statics: {
    dataProps: PropTypes.shape(dataProps),
  },

  propTypes: {
    ...dataProps,
    onChangeName: PropTypes.func.isRequired,
    onChangeGender: PropTypes.func.isRequired,
    onChangeHat: PropTypes.func.isRequired,
    onChangeHair: PropTypes.func.isRequired,
    onChangeOutfit: PropTypes.func.isRequired,
  },

  render() {
    return (
      <div className="row">
        <div className="col-md-1">
          <div className="form-group">
            <CycleButtons onCycle={dir => this.props.onChangeHat(dir)} />
          </div>

          <div className="form-group">
            <CycleButtons onCycle={dir => this.props.onChangeHair(dir)} />
          </div>

          <div className="form-group">
            <CycleButtons onCycle={dir => this.props.onChangeOutfit(dir)} />
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
                    onChange={event => this.props.onChangeGender(gender)}
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
                onChange={event => this.props.onChangeName(event.target.value)} />
            </label>
          </div>
        </div>
      </div>
    );
  },
});

module.exports = CharacterEditor;
