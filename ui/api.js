import React from 'react'
import PropTypes from 'prop-types'

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
    // Submits a new annotation for a specific document. The first argument is
    // the URL fragment returned by Cadastre; this can be pointed at the wrong
    // thing and will do wrong things in return if you do that.
    annotate(url, program) {
        const message = {
            program,
        }
        return this.postJson(message)
    }

    // Loads a specific annotation. The first argument is the URL fragment
    // returned by Cadastre; this can be pointed at the wrong thing and will do
    // wrong things in return if you do that.
    annotation(url) {
        return this.getJson(url)
    }

    // GET a JSON document. This is exposed for utility, but should rarely be
    // called by clients - the higher-level task-specific methods of this object
    // are more appropriate.
    getJson(url) {
        return fetch(url)
            .then(resp => resp.json())
    }

    // Fetches and decodes an original document. The argument is the URL
    // fragment returned by Cadastre; this can be pointed at the wrong thing and
    // will do wrong things in return if you do that.
    original(url) {
        return fetch(url)
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
        return fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: payload,
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
        const api = new Api()

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
