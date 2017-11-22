import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'

import Glyphicon from '../glyphicon'
import actions, { reducer } from './actions'
import StateNode from './nodes'
import { Tabbed, NavTab } from './tabbed'

import { withApi } from '../api'

export { reducer }

function StateEvents({events}) {
    return <table className="table table-striped table-condensed">
        <thead>
            <tr>
                <th className="col-md-2">Timestamp</th>
                <th className="col-md-1">Office</th>
                <th className="col-md-9">Message</th>
            </tr>
        </thead>
        <tbody>
            {
                events && events.map((event, index) =>
                    <tr key={index}>
                        <td>{event.timestamp}</td>
                        <td>{event.office}</td>
                        <td><Link to={`/document/${event.message_id}`}>{event.message}</Link></td>
                    </tr>)
            }
        </tbody>
    </table>
}

function StateWarnings({warnings}) {
    return <table className="table table-striped table-condensed">
        <thead>
            <tr>
                <th className="col-md-12">Message</th>
            </tr>
        </thead>
        <tbody>
            {
                warnings && warnings.map((warning, index) =>
                    <tr key={index}>
                        <td><Link to={`/document/${warning.message_id}`}>{warning.message}</Link></td>
                    </tr>)
            }
        </tbody>
    </table>
}

export default class State extends React.Component {
    componentDidMount() {
        this.props.loadState(this.props.api)
    }

    render() {
        return <div className="container">
            <ul className="nav nav-tabs">
                <NavTab name='state' active={this.props.tab} switchTab={this.props.switchTab}>
                    State
                </NavTab>
                <NavTab name='events' active={this.props.tab} switchTab={this.props.switchTab}>
                    Events
                </NavTab>
                <NavTab name='warnings' active={this.props.tab} switchTab={this.props.switchTab}>
                    Warnings {this.props.warnings.length > 0 &&
                        <Glyphicon name="fire" />
                    }
                </NavTab>
            </ul>

            <Tabbed name='state' active={this.props.tab}>
                <div className="row">
                    <div className="col-md-12">
                        <StateNode value={this.props.state} root={true} />
                    </div>
                </div>
            </Tabbed>

            <Tabbed name='events' active={this.props.tab}>
                <div className="row">
                    <div className="col-md-12">
                        <StateEvents {...this.props} />
                    </div>
                </div>
            </Tabbed>

            <Tabbed name='warnings' active={this.props.tab}>
                <div className="row">
                    <div className="col-md-12">
                        <StateWarnings {...this.props} />
                    </div>
                </div>
            </Tabbed>
        </div>
    }
}

State.connect = connect(
    ({state}) => state,
    actions,
)(withApi(State))
