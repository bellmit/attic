'use strict'

import React from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import DocumentTitle from 'react-document-title'
import classNames from 'classnames'

import { withApi } from 'app/api'

import * as actions from '../actions'
import CharacterEditor from './CharacterEditor'

function SquadEditor({characters, workflow, api, saveSquad, ...props}) {
  return <div className="container">
    <h1>Your Squad</h1>
    {characters.reduce(
      (elements, character, index) => {
        return elements.length == 0 ? [
          <CharacterEditor
            key={`character-${index}`}
            index={index}
            {...character}
            {...props} />
        ] : [
          ...elements,
          <hr key={`hr-${index}`} />,
          <CharacterEditor
            index={index}
            key={`character-${index}`}
            {...character}
            {...props} />
        ]
      },
      []
    )}
    <button
      className={classNames({
        'btn': true,
        'btn-primary': true,
        'btn-save': true,
        'disabled': workflow.saving || workflow.loading,
      })}
      disabled={workflow.saving || workflow.loading}
      onClick={() => saveSquad(api)}>
      {workflow.saving ? "Saving…" : workflow.loading ? "Loading…" : "Save & return to lobby"}
    </button>
  </div>
}

function mapStateToProps(state) {
  return state.squad
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(actions, dispatch)
}

const SquadEditorPage = connect(
  mapStateToProps,
  mapDispatchToProps
)(withApi(props => <DocumentTitle title="Your Squad">
  <SquadEditor {...props} />
</DocumentTitle>))

SquadEditorPage.Component = SquadEditor

module.exports = SquadEditorPage
