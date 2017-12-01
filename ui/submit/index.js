import React from 'react'
import ReactDOM from 'react-dom'
import FileDrop from 'react-file-drop'
import { connect } from 'react-redux'
import { withApi } from '../api'

import actions, { reducer } from './actions'

export { reducer }

function nullIf(candidate, value) {
    if (candidate == value)
        return null
    return candidate
}

function SubmissionEditor({api, document, contentType, messageId, date, changeContentType, changeMessageId, changeDate, submitDocument}) {
    function submit() {
        return submitDocument(
            api,
            document,
            contentType,
            nullIf(messageId, ''),
            nullIf(date, '')
        )
    }
    return <form onSubmit={submit}>
        <div className="form-group">
            <label htmlFor="submission-content-type">Document MIME Type</label>
            <input type="text"
                className="form-control"
                id="submission-content-type"
                placeholder="MIME Type"
                value={contentType}
                onChange={evt => changeContentType(evt.target.value)} />
                <p className="help-block">
                    This should be a registered MIME type. For email documents,
                    use <code>message/rfc822</code>.
                </p>
        </div>
        <div className="form-group">
            <label htmlFor="submission-message-id">Message ID</label>
            <input type="email"
                className="form-control"
                id="submission-message-id"
                placeholder="(Detect message ID)"
                value={messageId}
                onChange={evt => changeMessageId(evt.target.value)} />
        </div>
        <div className="form-group">
            <label htmlFor="submission-date">Effective Date</label>
            <input type="datetime-local"
                className="form-control"
                id="submission-date"
                placeholder="(Detect date)"
                value={date}
                onChange={evt => changeDate(evt.target.value)} />
        </div>
        <button type="submit" disabled={!document} className="btn btn-default">Submit Document</button>
    </form>
}

function SubmissionDropTarget({document, changeDocument}) {
    return document ?
        <FileDrop targetAlwaysVisible={true} onDrop={files => changeDocument(files[0])}>
            <div className="well">
                <p>Excellent! You're ready to submit <strong>{document.name}</strong>.</p>
                <p>Drop another document here to replace this document.</p>
            </div>
        </FileDrop> :
        <FileDrop targetAlwaysVisible={true} onDrop={files => changeDocument(files[0])}>
            <div className="well">
                Drop a document here to select it for submission.
            </div>
        </FileDrop>
}

export default class Submit extends React.Component {
    componentWillUnmount() {
        this.props.clearSubmission()
    }

    render() {
        return <div className="container">
            <div className="row">
                <div className="col-md-8">
                    <SubmissionDropTarget {...this.props} />
                </div>
                <div className="col-md-4">
                    <SubmissionEditor {...this.props} />
                </div>
            </div>
        </div>
    }
}

Submit.connect = connect(
    ({submit}) => submit,
    actions,
)(withApi(Submit))
