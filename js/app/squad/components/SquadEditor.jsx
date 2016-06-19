'use strict';

import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import DocumentTitle from 'react-document-title';
import classNames from 'classnames';

import { withApi } from 'app/api';

import * as actions from '../actions';
import CharacterEditor from './CharacterEditor';

const SquadEditor = React.createClass({
  componentWillMount() {
    this.props.actions.loadSquad(this.props.api);
  },

  render() {
    var {characters, actions, workflow, api} = this.props;

    return <DocumentTitle title="Your Squad">
      <div className="container">
        <h1>Your Squad</h1>
        {characters.reduce(
          (elements, character, index) => {
            return elements.length == 0 ? [
              <CharacterEditor
                key={`character-${index}`}
                index={index}
                {...character}
                {...actions} />
            ] : [
              ...elements,
              <hr key={`hr-${index}`} />,
              <CharacterEditor
                index={index}
                key={`character-${index}`}
                {...character}
                {...actions} />
            ]
          },
          []
        )}
        <button
          className={classNames("btn", "btn-primary", {disabled: workflow.saving || workflow.loading})}
          disabled={workflow.saving || workflow.loading}
          onClick={() => actions.saveSquad(api)}>
          {workflow.saving ? "Saving…" : workflow.loading ? "Loading…" : "Save & return to lobby"}
        </button>
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
)(withApi(SquadEditor));
