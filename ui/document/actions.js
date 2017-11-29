import { createAction, handleActions } from 'redux-actions'

const injectDocument = createAction('INJECT_DOCUMENT')
const originalLoaded = createAction('ORIGINAL_LOADED')
const notAnnotated = createAction('NOT_ANNOTATED')
const annotationLoaded = createAction('ANNOTATION_LOADED')
const changeAnnotation = createAction('CHANGE_ANNOTATION')
const forgetDocument = createAction('FORGET_DOCUMENT')
const savingAnnotation = createAction('SAVING_ANNOTATION')
const savedAnnotation = createAction('SAVED_ANNOTATION')
const saveAnnotationFailed = createAction('SAVE_ANNOTATION_FAILED')

function loadOriginal(api, meta) {
    return dispatch =>
        api.original(meta.current_revision.download_url)
            .then(original => dispatch(originalLoaded(original)))
}

function loadAnnotation(api, meta) {
    if (meta.current_annotation) {
        return dispatch =>
            api.annotation(meta.current_annotation.download_url)
                .then(json => json.program)
                .then(program => dispatch(annotationLoaded(program)))
    } else {
        return notAnnotated()
    }
}

function loadDocument(api, meta) {
    return dispatch => {
        dispatch(injectDocument(meta))
        dispatch(loadOriginal(api, meta))
        dispatch(loadAnnotation(api, meta))
    }
}

function fetchDocument(api, messageId) {
    return dispatch =>
        api.document(messageId)
            .then(json => dispatch(loadDocument(api, json)))
}

function saveAnnotation(api, url, program) {
    return dispatch => {
        dispatch(savingAnnotation())
        api.annotate(url, program)
            .then(() => dispatch(savedAnnotation()))
            .catch(() => dispatch(saveAnnotationFailed()))
    }
}

export const reducer = handleActions({
    [injectDocument]: (state, action) => ({
        ...state,
        meta: action.payload,
    }),
    [originalLoaded]: (state, action) => ({
        ...state,
        original: action.payload,
    }),
    [annotationLoaded]: (state, action) => ({
        ...state,
        annotation: action.payload,
        dirty: false,
    }),
    [notAnnotated]: (state, action) => ({
        ...state,
        annotation: "",
        dirty: true,
    }),
    [changeAnnotation]: (state, action) => ({
        ...state,
        annotation: action.payload,
        dirty: true,
    }),
    [savingAnnotation]: (state, action) => ({
        ...state,
        saving: true,
    }),
    [savedAnnotation]: (state, action) => ({
        ...state,
        saving: false,
        dirty: false,
    }),
    [saveAnnotationFailed]: (state, action) => ({
        ...state,
        saving: false,
    }),
    [forgetDocument]: (state, action) => ({
        original: null,
        meta: null,
        annotation: null,
        dirty: false,
    }),
}, {
    // The original document for the current revision of the underlying stored
    // document.
    original: null,
    // The document metadata for the current document.
    meta: null,
    // The current annotation for the current document.
    annotation: null,
    // If true, the annotation has changed since it was last loaded, or the
    // document had no annotation.
    dirty: false,
    // If true, a save is in progress.
    saving: false,
})

export default {
    // Given an existing document metadata object, load the original document
    // for the most recent revision and its most recent annotation, if it has
    // one. If you have a message ID, instead of a document metadata object, use
    // the `fetchDocument` action constructor.
    loadDocument,
    // Given a message ID, fetch the document metadata for that document, and
    // load it as if by `loadDocument`.
    fetchDocument,
    // Throw away all state relating to the current document.
    forgetDocument,
    // Update the pending annotation, setting editor dirty flags as appropriate.
    changeAnnotation,
    // Save the pending annotation to a given URL, updating the editor's dirty
    // flags.
    saveAnnotation,
}
