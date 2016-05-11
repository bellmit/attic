'use strict';

// For some insane reason, React isn't auto-required by babel when compiling
// jsx element expressions.
import React from 'react';
import ReactDOM from 'react-dom';
import { Router, browserHistory } from 'react-router';
import routes from './routes';

ReactDOM.render(
    <Router routes={routes} history={browserHistory} />,
    document.getElementById('app')
);
