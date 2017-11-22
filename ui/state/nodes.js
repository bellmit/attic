import React from 'react'
import ReactDOM from 'react-dom'
import _ from 'lodash'

import Glyphicon from '../glyphicon'

// Renders a toggled-in-place button for collapsing or uncollapsing something.
function CollapseButton({collapsed, toggleCollapsed}) {
    return <button
        type="button"
        className="btn btn-default btn-xs"
        onClick={toggleCollapsed}>
        {
            collapsed ?
                <Glyphicon name="expand" /> :
                <Glyphicon name="collapse-up" />
        }
    </button>
}

// Renders a single row for an object property, recursively rendering the value
// of that property.
function StateProperty({name, value}) {
    return <tr>
        <td className="col-md-1" style={{verticalAlign: 'top'}}>{name}</td>
        <td><StateNode value={value} /></td>
    </tr>
}

// Renders an object-shaped state value. This renders as a collaspable table of
// object properties, recursively rendering their values.
//
// For reasons of sheer laziness, the collapse process does not participate in
// Redux state management.
class StateObject extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            collapsed: props.collapsed,
        }
    }

    toggleCollapsed() {
        this.setState({
            collapsed: !this.state.collapsed,
        })
    }

    render() {
        return <div>
            { this.props.collapsable &&
                <CollapseButton
                    collapsed={this.props.collapsed}
                    toggleCollapsed={() => this.toggleCollapsed()} />
            }
            { !this.state.collapsed &&
                <table className="table table-striped table-condensed">
                    <tbody>
                        {
                            _.toPairs(this.props.value).sort().map(([name, value], index) =>
                                <StateProperty key={index} name={name} value={value} />)
                        }
                    </tbody>
                </table>
            }
        </div>
    }
}

// Renders an array-shaped state value. This renders as a non-collapsable table.
// (Why? Again: laziness.) The values are rendered recursively.
function StateArray({value}) {
    return <table className="table table-striped table-condensed">
        {
            value.map((value, index) =>
                <tr key={index}>
                    <td><StateNode value={value} /></td>
                </tr>
            )
        }
    </table>
}

// Renders a literal-shaped state value.
function StateLiteral({value}) {
    return <div style={{whiteSpace: 'pre-wrap'}}>{value}</div>
}

// Renders a tree of state values. Pass root=true to force this node to expand.
export default function StateNode({value, root}) {
    if (value instanceof Array) {
        return <StateArray value={value} />
    }
    if (value instanceof Object) {
        return <StateObject collapsable={!root} collapsed={!root} value={value} />
    }
    return <StateLiteral value={value} />
}
