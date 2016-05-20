'use strict';

import React from 'react';
import DocumentTitle from 'react-document-title';

function App({children}) {
  return <DocumentTitle title="Distant Shore">
    {children}
  </DocumentTitle>;
}

module.exports = App;