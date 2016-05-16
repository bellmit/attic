'use strict';

import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import DocumentTitle from 'react-document-title';

import * as actions from '../actions';
import CharacterEditor from './CharacterEditor';

const SquadEditor = React.createClass({
  render() {
    return <DocumentTitle title="Your Squad">
      <div className="container">
        <h1>Your Squad</h1>
        {this.props.characters.reduce(
          (elements, character, index) => {
            return elements.length == 0 ? [
              <CharacterEditor
                key={`character-${index}`}
                index={index}
                {...character}
                {...this.props.actions} />
            ] : [
              ...elements,
              <hr key={`hr-${index}`} />,
              <CharacterEditor
                index={index}
                key={`character-${index}`}
                {...character}
                {...this.props.actions} />
            ]
          },
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
    actions: bindActionCreators(actions, dispatch),
  };
}

module.exports = connect(
  mapStateToProps,
  mapDispatchToProps
)(SquadEditor);
