'use strict';

import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { connect } from 'react-redux';
import DocumentTitle from 'react-document-title';

import * as actions from '../actions';
import CharacterEditor from './CharacterEditor';

const SquadEditor = React.createClass({
  propTypes: {
    characters: PropTypes.arrayOf(CharacterEditor.dataProps.isRequired).isRequired,
    onChangeCharacterName: PropTypes.func.isRequired,
    onChangeCharacterGender: PropTypes.func.isRequired,
    onChangeCharacterHat: PropTypes.func.isRequired,
    onChangeCharacterHair: PropTypes.func.isRequired,
    onChangeCharacterOutfit: PropTypes.func.isRequired,
  },
  
  render() {
    return <DocumentTitle title="Your Squad">
      <div className="container">
        <h1>Your Squad</h1>
        {this.props.characters.reduce((elements, character, index) =>
          elements.length == 0 ? [
            <CharacterEditor
              key={`character-${index}`}
              onChangeName={name => this.props.onChangeCharacterName(index, name)}
              onChangeGender={gender => this.props.onChangeCharacterGender(index, gender)}
              onChangeHat={direction => this.props.onChangeCharacterHat(index, direction)}
              onChangeHair={direction => this.props.onChangeCharacterHair(index, direction)}
              onChangeOutfit={direction => this.props.onChangeCharacterOutfit(index, direction)}
              {...character} />
          ] : [
            ...elements,
            <hr key={`hr-${index}`} />,
            <CharacterEditor
              key={`character-${index}`}
              onChangeName={name => this.props.onChangeCharacterName(index, name)}
              onChangeGender={gender => this.props.onChangeCharacterGender(index, gender)}
              onChangeHat={direction => this.props.onChangeCharacterHat(index, direction)}
              onChangeHair={direction => this.props.onChangeCharacterHair(index, direction)}
              onChangeOutfit={direction => this.props.onChangeCharacterOutfit(index, direction)}
              {...character} />
          ],
          []
        )}
        <Link className="btn btn-primary" to="/">
          Save &amp; return to lobby
        </Link>
      </div>
    </DocumentTitle>;
  },
});

function mapStateToProps(state) {
  return state.squad;
}

function mapDispatchToProps(dispatch) {
  return {
    onChangeCharacterName(index, name) {
      dispatch(actions.changeCharacterName(index, name));
    },
    onChangeCharacterGender(index, gender) {
      dispatch(actions.changeCharacterGender(index, gender));
    },
    onChangeCharacterHat(index, direction) {
      dispatch(actions.changeCharacterHat(index, direction));
    },
    onChangeCharacterHair(index, direction) {
      dispatch(actions.changeCharacterHair(index, direction));
    },
    onChangeCharacterOutfit(index, direction) {
      dispatch(actions.changeCharacterOutfit(index, direction));
    },
  };
}

module.exports = connect(
  mapStateToProps,
  mapDispatchToProps
)(SquadEditor);
