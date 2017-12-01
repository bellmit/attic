import { createAction, handleActions } from 'redux-actions'
import sorted, { stringCompare } from './sorted'

const documentsLoading = createAction('DOCUMENTS_LOADING')
const documentsLoaded = createAction('DOCUMENTS_LOADED')
const sortDocuments = createAction('SORT_DOCUMENTS')

function fetchDocuments(api) {
    return dispatch => {
        dispatch(documentsLoading())
        return api.documents()
            .then(json => dispatch(documentsLoaded(json)))
    }
}

export default {
    // Fetch or re-fetch the list of documents from the service.
    fetchDocuments,
    // Change the sorting of fetched documents.
    sortDocuments,
}

const documentComparators = {
    message_id(a, b) {
        return stringCompare(a.message_id, b.message_id)
    },
    date(a, b) {
        return stringCompare(a.current_revision.date, b.current_revision.date)
    },
    annotated(a, b) {
        if (a.current_annotation && b.current_annotation)
            return 0
        if (a.current_annotation)
            return -1
        if (b.current_annotation)
            return 1
    }
}

export const reducer = handleActions({
    [documentsLoading]: (state, action) => ({
        ...state,
        loading: true,
    }),
    [documentsLoaded]: (state, action) => ({
        ...state,
        loading: false,
        documents: sorted(action.payload, documentComparators, state.sort, state.sortReverse),
    }),
    [sortDocuments]: (state, action) => (action.payload != state.sort ? {
        ...state,
        sort: action.payload,
        sortReverse: false,
        documents: sorted(state.documents, documentComparators, action.payload, false),
    } : {
        ...state,
        sort: action.payload,
        sortReverse: !state.sortReverse,
        documents: sorted(state.documents, documentComparators, action.payload, !state.sortReverse),
    }),
}, {
    // If true, indicates that a load is in progress.
    loading: false,
    // The list of documents to render (or null before the list has been loaded
    // once)
    documents: null,
    // The current sort property, if any.
    sort: 'date',
    // Reverses the sorting if true.
    sortReverse: true,
})
