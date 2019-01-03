import { createAction, handleActions } from 'redux-actions'
import { push } from 'react-router-redux'

const changeDocument = createAction('CHANGE_DOCUMENT')
const changeContentType = createAction('CHANGE_CONTENT_TYPE')
const changeMessageId = createAction('CHANGE_MESSAGE_ID')
const changeSubject = createAction('CHANGE_SUBJECT')
const changeDate = createAction('CHANGE_DATE')
const clearSubmission = createAction('CLEAR_SUBMISSION')
const submitting = createAction('SUBMITTING')
const submitFailed = createAction('SUBMIT_FAILED')

function submitDocument(api, document, contentType, messageId, date, subject) {
    return dispatch => {
        dispatch(submitting())
        api.submitDocument(document, contentType, messageId, date, subject)
            .then(json => json.message_id)
            .then(messageId => dispatch(push(`/document/${messageId}`)))
            .catch(json => dispatch(submitFailed()))
    }
}

export default {
    changeDocument,
    changeContentType,
    changeMessageId,
    changeSubject,
    changeDate,
    clearSubmission,
    submitDocument,
}

export const reducer = handleActions({
    [changeDocument]: (state, action) => ({
        ...state,
        contentType: action.payload.type,
        document: action.payload,
    }),
    [changeContentType]: (state, action) => ({
        contentType: action.payload,
    }),
    [changeMessageId]: (state, action) => ({
        ...state,
        messageId: action.payload,
    }),
    [changeSubject]: (state, action) => ({
        ...state,
        subject: action.payload,
    }),
    [changeDate]: (state, action) => ({
        ...state,
        date: action.payload,
    }),
    [clearSubmission]: (state, action) => ({
        ...state,
        document: null,
        submitting: false,
        contentType: '',
        messageId: '',
        date: '',
        subject: '',
    }),
    [submitting]: (state, action) => ({
        ...state,
        submitting: true,
    }),
    [submitFailed]: (state, action) => ({
        ...state,
        submitting: false,
    }),
}, {
    document: null,
    submitting: false,
    contentType: '',
    messageId: '',
    date: '',
    subject: '',
})
