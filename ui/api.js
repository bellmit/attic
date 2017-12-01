import React from 'react'
import PropTypes from 'prop-types'
import { encode } from 'base-64'

function basicCredentials(username, password) {
    const credentialsPair = encode(`${username}:${password}`)
    const header = `Basic ${credentialsPair}`
    return {
        'Authorization': header,
    }
}

function tokenCredentials(token) {
    if (!token)
        return {}

    return {
        'Authorization': `Bearer ${token}`,
    }
}

function throwHttpErrors(resp) {
    if (resp.status >= 400)
        throw resp
    return resp
}

// Cadastre API client
//
// Calls to Cadastre use these helpers to streamline and centralize handling of
// common concerns:
//
// * Message body encoding and decoding
// * Authentication and authorization
// * URL mapping for various requests
//
// All API results are returned as promises, to abstract over details around
// error handling and decoding.

// Entry point for calling back to Cadastre.
export default class Api {
    constructor(token=null) {
        this.token = token
        this.authHook = null
    }

    // Creates an API instance using the last token saved with
    // `saveCredentials`, if any.
    static withSavedCredentials() {
        const token = localStorage.getItem('token')
        return new Api(token)
    }

    // Registers an auth hook. If a request fails, the auth hook will be called,
    // with one argument (a function which retries the failed request).
    setAuthHook(authHook) {
        this.authHook = authHook
    }

    // Update the token used on future requests, including pending retries.
    setToken(token) {
        this.token = token
    }

    // Write the current credentials to localStorage. Future calls to
    // `withSavedCredentials` will recover this token.
    saveCredentials() {
        localStorage.setItem('token', this.token)
    }

    // Submits a new annotation for a specific document. The first argument is
    // the URL fragment returned by Cadastre; this can be pointed at the wrong
    // thing and will do wrong things in return if you do that.
    annotate(url, program) {
        const message = {
            program,
        }
        return this.postJson(url, message)
    }

    // Loads a specific annotation. The first argument is the URL fragment
    // returned by Cadastre; this can be pointed at the wrong thing and will do
    // wrong things in return if you do that.
    annotation(url) {
        return this.getJson(url)
    }

    // A retryable fetch. All request methods on this class, other than those
    // directly related to login, are implemented in terms of this - it provides
    // core error handling (primarily, login-related error handling).
    fetch(url, opts={}) {
        const headers = opts.headers || {}
        const attempt = () =>
            fetch(url, {
                ...opts,
                headers: {
                    ...tokenCredentials(this.token),
                    ...headers,
                },
            })
                .then(throwHttpErrors)
                .catch(authRetry)
        const authRetry = err => new Promise((resolve, reject) => {
            const retry = () => resolve(attempt())
            const cancel = () => reject(err)

            if (!this.authHook)
                cancel()
            else if (err.status != 403)
                cancel()
            else
                this.authHook(retry, cancel)
        })
        return attempt()
    }

    // GET a JSON document. This is exposed for utility, but should rarely be
    // called by clients - the higher-level task-specific methods of this object
    // are more appropriate.
    getJson(url, opts={}) {
        return this.fetch(url, opts)
            .then(resp => resp.json())
    }

    // Log into Cadastre with an existing account. This method does not
    // automatically retry on errors!
    login(email, password) {
        const message = {
            description: `Cadastre Web UI (on ${new Date()})`,
        }
        const payload = JSON.stringify(message)
        return fetch('/user/token', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...basicCredentials(email, password),
            },
            body: payload,
        })
            .then(throwHttpErrors)
            .then(resp => resp.json())
    }

    // Fetches and decodes an original document. The argument is the URL
    // fragment returned by Cadastre; this can be pointed at the wrong thing and
    // will do wrong things in return if you do that.
    original(url) {
        return this.fetch(url)
            .then(resp => resp.blob())
            .then(blob => new Promise(resolve => {
                const reader = new FileReader()
                reader.addEventListener("loadend", () => resolve(reader.result))
                reader.readAsArrayBuffer(blob)
            }))
            .then(buffer => {
                // This almost certainly isn't valid, since we can't guarantee
                // that the incoming buffer is valid UTF-8. However, in order to
                // display documents, we need text; this is a reasonable guess
                // for code that doesn't have a full MIME implementation
                // available.
                const decoder = new TextDecoder('UTF-8')
                return decoder.decode(buffer)
            })
    }

    // POST a JSON document. This is exposed for utility, but should rarely be
    // called by clients - the higher-level task-specific methods of this object
    // are more appropriate.
    postJson(url, message) {
        const payload = JSON.stringify(message)
        return this.fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: payload,
        })
    }

    // Log into Cadastre with a new account. This method does not automatically
    // retry on errors!
    register(email, password) {
        const message = {
            email,
            password,
            token_description: `Cadastre Web UI (on ${new Date()})`,
        }
        const payload = JSON.stringify(message)
        return fetch('/user/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: payload,
        })
            .then(throwHttpErrors)
            .then(resp => resp.json())
    }

    // Submits a document to the registry.
    submitDocument(document, contentType, messageId, date) {
        const messageIdHeader = messageId ? {
            'Message-ID': messageId,
        } : {}
        const dateHeader = date ? {
            'Date': date,
        } : {}
        return this.getJson('/document', {
            method: 'POST',
            headers: {
                'Content-Type': contentType,
                ...messageIdHeader,
                ...dateHeader,
            },
            body: document,
        })
    }

    // Fetches document metadata for a specific document, by message ID.
    document(messageId) {
        return this.getJson(`/document/${messageId}`)
    }

    // Fetches the list of documents from the service.
    documents() {
        return this.getJson('/document')
    }

    // Fetches the calculated state from the service.
    state() {
        return this.getJson('/state')
    }
}

const apiContextProps = {
    api: PropTypes.instanceOf(Api),
}



// Provides a single API instance, shared with all `withApi`-wrapped components
// contained within it.
export class ApiProvider extends React.Component {
    static get childContextTypes() {
        return apiContextProps
    }

    getChildContext() {
        const api = Api.withSavedCredentials()

        return {api,}
    }

    render() {
        return this.props.children
    }
}

// Provides an instance of the Api class to the wrapped component.
export function withApi(WrappedComponent, as='api') {
    const wrappedName = WrappedComponent.displayName || WrappedComponent.name || 'Component'
    return class extends React.Component {
        static get displayName() {
            return `withApi(${wrappedName})`
        }

        static get contextTypes() {
            return apiContextProps
        }

        render() {
            const {api} = this.context
            const apiProps = {
                [as]: api,
            }

            return <WrappedComponent {...apiProps} {...this.props} />
        }
    }
}
