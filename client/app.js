import React from 'react'
import ReactDOM from 'react-dom'
import reducers from '../pacha/reducers'

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

const pulse = async line => {
    console.log('posting')
    await fetch('/api/command', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json',
        }),
        body: JSON.stringify({
            line,
        }),
    })
    console.log('posted')
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

const ChatLine = ({line}) => line ? <p>{line}</p> : null

const render = () => ReactDOM.render(
    <div>
        <Line onLine={line => pulse(line)}>Pulse</Line>
        { state && state.events.map((event, idx) => <ChatLine key={idx} {...event} />) }
    </div>,
    document.getElementById('app'),
)

render()
