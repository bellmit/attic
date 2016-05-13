'use strict';

import React from 'react';
import DocumentTitle from 'react-document-title';

module.exports = React.createClass({
  render() {
    return (
      <DocumentTitle title="Distant Shore">
        {this.props.children}
      </DocumentTitle>
    );
  },
});
