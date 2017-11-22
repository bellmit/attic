import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'

import Glyphicon from '../glyphicon'

import { withApi } from '../api'
import actions, { reducer } from './actions'

export { reducer }

// Render a summary row for a single document.
function DocumentSummary({document}) {
    return <tr>
        {
            document.current_annotation ?
                <td><Glyphicon name="ok" /></td> :
                <td />
        }
        <td><Link to={{
            pathname: `/document/${document.message_id}`,
            state: document,
        }}>{document.message_id}</Link></td>
        <td>{document.current_revision.date}</td>
    </tr>
}

// Extra navigation buttons for this view. This consists of the refresh button,
// allowing the user to trigger a reload of the document list without leaving
// this view.
export function Navbar({api, fetchDocuments, loading}) {
    return <form className="navbar-form navbar-left">
        <button
            type="button"
            className="btn btn-default"
            title="Refresh Documents"
            onClick={() => fetchDocuments(api)}
            disabled={loading}>
            <Glyphicon name="refresh" />
        </button>
    </form>
}

// As above, but connected to Redux so that it can respond to state changes.
Navbar.connect = connect(
    ({documents}) => documents,
    actions,
)(withApi(Navbar))

function SortIcon({name, sort, reverse}) {
    if (name == sort)
        return <Glyphicon name={reverse ? "chevron-down" : "chevron-up"} />
    return null
}

function DocumentsTable({documents, sort, sortReverse, sortDocuments}) {
    const tableBody = documents ?
        documents.map(document => <DocumentSummary key={document.message_id} document={document} />) :
        <tr>
            <td colSpan="3" className="text-center text-muted">Loading document list...</td>
        </tr>

    return <table className="table table-striped table-condensed">
        <thead>
            <tr>
                <th className="col-md-1" style={{cursor: 'pointer'}} onClick={() => sortDocuments('annotated')}>
                    An <SortIcon name='annotated' sort={sort} reverse={sortReverse} />
                </th>
                <th style={{cursor: 'pointer'}} onClick={() => sortDocuments('message_id')}>
                    Message ID <SortIcon name='message_id' sort={sort} reverse={sortReverse} />
                </th>
                <th className="col-md-2" style={{cursor: 'pointer'}} onClick={() => sortDocuments('date')}>
                    Date <SortIcon name='date' sort={sort} reverse={sortReverse} />
                </th>
            </tr>
        </thead>
        <tbody>
            {tableBody}
        </tbody>
    </table>
}

export default class Documents extends React.Component {
    componentDidMount() {
        this.props.fetchDocuments(this.props.api)
    }

    render() {
        return <DocumentsTable {...this.props} />
    }
}

Documents.connect = connect(
    ({documents}) => documents,
    actions,
)(withApi(Documents))
