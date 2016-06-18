'use strict';

import React from 'react';
import DocumentTitle from 'react-document-title';

import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { withApi } from 'app/api';
import { boot } from 'app/lock/actions';

const App = React.createClass({
  componentDidMount() {
    this.props.boot(this.props.api);
  },

  render() {
    var {booting, children} = this.props;

    return <DocumentTitle title="Distant Shore">
    { !booting &&
      children
    }
    </DocumentTitle>;
  },
});

module.exports = connect(
  state => state.lock,
  dispatch => bindActionCreators({boot}, dispatch)
)(withApi(App));
