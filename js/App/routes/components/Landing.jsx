'use strict';

import React from 'react';
import { Link } from 'react-router';

module.exports = React.createClass({
    render() {
        return (
            <ul>
                <li><Link to="/squad">Squad</Link></li>
            </ul>
        );
    },
});
