import React from 'react'
import ReactDOM from 'react-dom'
import { Link, Prompt } from 'react-router-dom'
import { connect } from 'react-redux'
import brace from 'brace'
import AceEditor from 'react-ace'

// Force the themes we use to load, to stop AceEditor from trying to load them
// dynamically.
import 'brace/mode/scheme'
import 'brace/theme/xcode'

import { withApi } from '../api'

import actions, { reducer } from './actions'

export { reducer }

// Renders a document's original. This is a dumb PRE of the document text.
function DocumentOriginal({original}) {
    return original != null ?
        <pre>{original}</pre> :
        <div className="text-muted text-center">Loading original document...</div>
}

// Renders an editor containing the document's current annotation, and publishes
// those changes back into the Redux store.
function DocumentEditor({annotation, dirty, saving, changeAnnotation}) {
    return [
        <Prompt
            key="document-dirty-prompt"
            when={dirty}
            message="You have unsaved annotation changes, are you sure you want to abandon them?" />,
        annotation != null ?
            <AceEditor
                key="document-editor"
                mode="scheme"
                theme="xcode"
                width="100%"
                height="600px"
                tabSize={2}
                wrapEnabled={true}
                readOnly={saving}
                value={annotation}
                onChange={changeAnnotation}
                editorProps={{$blockScrolling: Infinity}} /> :
            <div key="document-loading" className="text-muted text-center">Loading annotation...</div>,
    ]
}

// Additional top-level nav bar content to include when rendering the `Document`
// view. This provides the save button, in what may be a non-ideal location.
export function Navbar({api, dirty, saving, annotation, saveAnnotation, meta}) {
    return <form className="navbar-form navbar-left">
        <button
            className="btn btn-primary"
            disabled={/* xxx doesn't work */ true || !dirty && !saving}
            onClick={() => saveAnnotation(api, meta.annotate_url, annotation)}>Save</button>
    </form>
}

// As above, but wired into Redux so that it can respond to document state
// changes.
Navbar.connect = connect(
    ({document}) => document,
    actions,
)(withApi(Navbar))

// Renders the document editor. On mount, it attempts to load the document
// metadata, original, and annotations based on the message ID in the path, or
// using the document metadata provided via the routing state.
//
// When this component is removed from the DOM, it triggers Redux to forget the
// editor state.
export default class Document extends React.Component {
    componentDidMount() {
        if (this.props.location.state)
            this.props.loadDocument(this.props.api, this.props.location.state)
        else
            this.props.fetchDocument(this.props.api, this.props.match.params.messageId)
    }

    componentWillUnmount() {
        this.props.forgetDocument()
    }

    render() {
        return <div className="container-fluid">
            <div className="row">
                <div className="col-md-6">
                    <DocumentOriginal {...this.props} />
                </div>
                <div className="col-md-6">
                    <DocumentEditor {...this.props} />
                </div>
            </div>
        </div>
    }
}

// As above, but wired into Redux so that it can respond to document state
// changes.
Document.connect = connect(
    ({document}) => document,
    actions,
)(withApi(Document))
