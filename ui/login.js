import React from 'react'
import ReactDOM from 'react-dom'
import Modal from 'react-modal'

import { withApi } from './api'

// This does not take part in the Redux state. The biggest driver is that this
// needs to handle non-Redux state, like callbacks, but also: it was just plain
// easier to write this way.
export default class Login extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            open: false,
            retry: null,
            cancel: null,
            invalidCredentials: false,
            email: "",
            password: "",
        }

        this.props.api.setAuthHook((retry, cancel) => this.beginAuth(retry, cancel))
    }

    beginAuth(retry, cancel) {
        this.setState({
            open: true,
            retry,
            cancel,
        })
    }

    dismiss() {
        this.setState({
            open: false,
            retry: null,
            cancel: null,
            invalidCredentials: false,
            email: "",
            password: "",
        })
    }

    success(token) {
        this.props.api.setToken(token)
        this.props.api.saveCredentials()
        this.state.retry()
        this.dismiss()
    }

    cancel() {
        this.state.cancel()
        this.dismiss()
    }

    onEmailChanged(email) {
        this.setState({
            email,
        })
    }

    onPasswordChanged(password) {
        this.setState({
            password,
        })
    }

    invalidCredentials() {
        this.setState({
            invalidCredentials: true,
        })
    }

    login() {
        this.props.api.login(this.state.email, this.state.password)
            .then(json => this.success(json.token))
            .catch(() => this.invalidCredentials())
    }

    register() {
        this.props.api.register(this.state.email, this.state.password)
            .then(json => this.success(json.token))
            .catch(() => this.invalidCredentials())
    }

    render() {
        const submit = evt => {
            this.login()
            evt.preventDefault()
        }
        return <Modal
            style={{
                overlay: {
                    zIndex: 5, // hack; ensures this modal sits atop AceEditor
                },
                content: {
                    // The docs for react-modal claim that props are merged with
                    // the default. Testing determined that that was untrue
                    // (though it may be intended), so this is largely a copy of
                    // react-modal's defaults, other than dimensioning.
                    position: 'absolute',
                    top: '40px',
                    border: '1px solid #ccc',
                    background: '#fff',
                    overflow: 'auto',
                    WebkitOverflowScrolling: 'touch',
                    borderRadius: '4px',
                    outline: 'none',
                    padding: '20px',
                },
            }}
            className={{
                afterOpen: 'col-md-4 col-md-offset-4',
            }}
            isOpen={this.state.open}
            shouldCloseOnOverlayClick={false}>
            <form onSubmit={submit}>
                { this.state.invalidCredentials &&
                    <div className="alert alert-warning" role="alert">Invalid email or password.</div>
                }
                <div className="form-group">
                    <label htmlFor="login-email">Email address</label>
                    <input type="email"
                        className="form-control"
                        id="login-email"
                        placeholder="Email"
                        value={this.state.email}
                        onChange={evt => this.onEmailChanged(evt.target.value)} />
                </div>
                <div className="form-group">
                    <label htmlFor="login-password">Password</label>
                    <input type="password"
                        className="form-control"
                        id="login-password"
                        placeholder="Password"
                        value={this.state.password}
                        onChange={evt => this.onPasswordChanged(evt.target.value)} />
                </div>
                <div className="row">
                    <div className="col-md-4">
                        <button type="submit" className="btn btn-block btn-success">Log In</button>
                    </div>
                    <div className="col-md-4">
                        <button type="button" className="btn btn-block btn-primary" onClick={() => this.register()}>Create Account</button>
                    </div>
                    <div className="col-md-4">
                        <button type="button" className="btn btn-block btn-danger" onClick={() => this.cancel()}>Cancel Request</button>
                    </div>
                </div>
            </form>
        </Modal>
    }
}

Login.withApi = withApi(Login)
