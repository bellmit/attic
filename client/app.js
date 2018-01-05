import React from 'react'
import ReactDOM from 'react-dom'
import reducers from '../pacha/reducers'
import commands from '../pacha/commands'

let state = undefined
const events = new EventSource('/api/events')
events.addEventListener('state', evt => {
    state = JSON.parse(evt.data)
    render()
})
events.addEventListener('events', evt => {
    const events = JSON.parse(evt.data)
    state = events.reduce(reducers.event, state)
    render()
})

const enterGame = async name => {
    const payload = commands.joinGame(name)
    await fetch('/api/command', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json',
        }),
        body: JSON.stringify(payload),
    })
}

class Line extends React.Component {
    constructor(...args) {
        super(...args)
        this.state = {
            line: '',
        }
    }

    update(evt) {
        this.setState({
            line: evt.target.value,
        })
    }

    send(evt) {
        evt.preventDefault()
        this.props.onLine(this.state.line)
        this.setState({
            line: '',
        })
    }

    render() {
        return <form onSubmit={evt => this.send(evt)}>
            <input autoFocus onChange={evt => this.update(evt)} value={this.state.line} />
            <button type='submit'>{this.props.children}</button>
        </form>
    }
}

const render = () => ReactDOM.render(
    <div>
        <Line onLine={line => enterGame(line)}>Pulse</Line>
        <pre>{JSON.stringify(state, undefined, '  ')}</pre>
    </div>,
    document.getElementById('app'),
)

render()
